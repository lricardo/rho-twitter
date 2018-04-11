package pt.rhosystems.rhotwitter.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.List;

import okhttp3.ResponseBody;
import pt.rhosystems.rhotwitter.models.Tweet;
import pt.rhosystems.rhotwitter.api.TwitterStreamingApiHelper;
import pt.rhosystems.rhotwitter.models.User;
import pt.rhosystems.rhotwitter.utilities.ExpiringList;
import pt.rhosystems.rhotwitter.R;
import pt.rhosystems.rhotwitter.adapters.TweetAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import twitter4j.TwitterStreamFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private TwitterStreamFactory twitterStreamFactory;
    private TweetAdapter tweetAdapter;
    private List<Tweet> tweetList;
    private RecyclerView tweetRecyclerView;
    private SearchView searchView;

    private boolean isConnected = false;
    private TwitterStreamingApiHelper apiHelper;
    private Call<ResponseBody> currentCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tweetRecyclerView = findViewById(R.id.tweetList);
        int expireTime = Integer.parseInt(getString(R.string.expire_time));
        tweetList = new ExpiringList<>(expireTime);

        tweetAdapter = new TweetAdapter(this, tweetList);
        tweetRecyclerView.setAdapter(tweetAdapter);
        tweetRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        apiHelper = new TwitterStreamingApiHelper(this);
        //buildTwitterBaseConfiguration();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStatusReceiver, intentFilter);
    }



    /* UI Configuration Methods */

    /**
     * Configures the Menu, inflating the SearchView.
     * @param menu he options menu in which you place your items.
     * @return As super, the value must be true to be shown in the menu. It will not be shown
     * otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Utility method from notifying the ReyclerView that new data has been inserted
     * or deleted. This code runs on UI thread explicitaly, as result of being called in
     * other thread, disabling the usage of the method notifyDataSetChanged() directly.
     */
    private void refreshLayout () {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // This instruction prevents errors in RecyclerView
                // regarding the data-structure.
                tweetRecyclerView.getRecycledViewPool().clear();
                // Notify the adapter that the data has changed.
                tweetAdapter.notifyDataSetChanged();
            }
        });
    }

    /** SearchView Callbacks for the text searching capabilities **/

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (isConnected && !query.isEmpty()) {

            currentCall = apiHelper.api.track(query);

            currentCall.enqueue(responseBodyCallback);
            searchView.clearFocus();

            Toast.makeText(
                    this,
                    R.string.toast_on_query_stream_starting,
                    Toast.LENGTH_LONG)
                    .show();
        }
        else {
            Toast.makeText(
                    this,
                    R.string.toast_on_query_device_not_connected,
                    Toast.LENGTH_SHORT)
                    .show();
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (currentCall != null) {
            currentCall.cancel();
            Log.v("", "Call canceled.");
        }

        return false;
    }

    /* Network Status BroadCastReceiver */

    /**
     * This object listens to network changes.
     * I could have implemented a ConnectionLifeCycleListener (a callback for TwitterStream object),
     * but I think that it is too much abstraction (and I plan to implement my own interaction with
     * the service).
     */
    private BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("NetworkStatusReceiver", "Network state changed.");

            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager cm = (ConnectivityManager)
                        context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm != null) {
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

                    isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();

                    if (isConnected) {
                        Log.v("networkStatusReceiver", "Device is connected.");
                        isConnected = true;
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.toast_network_device_connected
                                , Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        Log.v("networkStatusReceiver", "Device is not Connected");
                        isConnected = false;
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.toast_network_device_not_connected,
                                Toast.LENGTH_LONG)
                                .show();

                        if (currentCall != null) {
                            currentCall.cancel();
                        }
                    }
                }
                else {
                    Log.e("networkStatusReceiver", "Unknown error.");
                }
            }

        }
    };

    /* Data Streaming */

    /**
     * This object listens to the stream, under the onResponse callback.
     * The InputStream is obtained from the response body and GSON is used for deserealizing
     * the objects and converting them to tweets. This callback is called once, as result of a
     * single request to the stream API which is kept open (see TwitterStreamingApiHelper timeout).
     * The objects are hence added to the tweetList.
     */
    private Callback<ResponseBody> responseBodyCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Log.v("------", "Response");
                if (response.isSuccessful()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JsonReader reader = new JsonReader(
                                        new InputStreamReader(response.body().byteStream()));
                                Gson gson = new GsonBuilder().create();

                                JsonObject j = gson.fromJson(reader, JsonObject.class);

                                User user = new User(
                                        j.getAsJsonObject("user").get("name").getAsString(),
                                        j.getAsJsonObject("user").get("screen_name").getAsString()
                                );
                                Tweet tweet = new Tweet(
                                        j.get("created_at").getAsString(),
                                        j.get("id_str").getAsString(),
                                        j.get("text").getAsString(),
                                        user
                                );

                                while (tweet != null) {
                                    if (tweet.getText() != null) {
                                        tweetList.add(tweet);

                                        Log.v("responseBodyCallback", tweet.getText());
                                        j = gson.fromJson(reader, JsonObject.class);

                                        if (j.getAsJsonObject("user") != null) {
                                            user = new User(
                                                    j.getAsJsonObject("user").get("name").getAsString(),
                                                    j.getAsJsonObject("user").get("screen_name").getAsString()
                                            );
                                            tweet = new Tweet(
                                                    j.get("created_at").getAsString(),
                                                    j.get("id_str").getAsString(),
                                                    j.get("text").getAsString(),
                                                    user
                                            );
                                        }
                                        else {
                                            Log.v("", "Received track notice...");
                                        }

                                        refreshLayout();
                                    } else {
                                        Log.v("responseBodyCallback", "Waiting for messages...");
                                    }
                                }
                            } catch (JsonSyntaxException e) {
                                Log.v("responseBodyCallback", "Stopped streaming.");
                            }
                        }
                    }).start();
                }

        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.e("responseBodyCallback", "Response failure.");
        }
    };

}
