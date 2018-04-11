package pt.rhosystems.rhotwitter.activities;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
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

import java.util.List;

import pt.rhosystems.rhotwitter.utilities.ExpiringList;
import pt.rhosystems.rhotwitter.R;
import pt.rhosystems.rhotwitter.adapters.StatusAdapter;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity implements StatusListener,
        SearchView.OnQueryTextListener {

    private TwitterStreamFactory twitterStreamFactory;
    private StatusAdapter statusAdapter;
    private List<Status> statusList;
    private RecyclerView tweetListRecyclerView;
    private TwitterStream currentStream;
    private SearchView searchView;

    private boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tweetListRecyclerView = findViewById(R.id.tweetList);
        int expireTime = Integer.parseInt(getString(R.string.expire_time));
        statusList = new ExpiringList<>(expireTime);

        statusAdapter = new StatusAdapter(this, statusList);
        tweetListRecyclerView.setAdapter(statusAdapter);
        tweetListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        buildTwitterBaseConfiguration();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStatusReceiver, intentFilter);
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
                        Log.v("NetworkStatusReceiver", "Device is connected.");
                        isConnected = true;
                        Toast.makeText(getApplicationContext(), "Start searching for streaming!"
                                , Toast.LENGTH_LONG).show();
                    }
                    else {
                        Log.v("NetworkStatusReceiver", "Device is not Connected");
                        isConnected = false;
                        Toast.makeText(getApplicationContext(), "The device is not connected",
                                Toast.LENGTH_LONG).show();
                        if (currentStream != null) {
                            currentStream.shutdown();
                        }
                    }
                }
                else {
                    Log.e("NetworkStatusReceiver", "Unknown error.");
                }
            }

        }
    };

    /* Twitter4j Related Configurations */

    /**
     * Build Twitter4j base configuration and initializes the activity TwitterStreamFactory
     * with the base authentication.
     */
    public void buildTwitterBaseConfiguration() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder
                .setOAuthConsumerKey(getBaseContext().getString(R.string.consumer_key))
                .setOAuthConsumerSecret(getBaseContext().getString(R.string.consumer_secret))
                .setOAuthAccessToken(getBaseContext().getString(R.string.access_token))
                .setOAuthAccessTokenSecret(getBaseContext().getString(R.string.access_token_secret));

        Configuration configuration = configurationBuilder.build();

        twitterStreamFactory = new TwitterStreamFactory(configuration);
    }

    /** OptionsMenu Configuration for the SearchButton **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    /** SearchView Callbacks for the text searching capabilities **/

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (isConnected) {
            currentStream = twitterStreamFactory.getInstance();
            currentStream.addListener(MainActivity.this);
            currentStream.filter(query);

            searchView.clearFocus();
        }
        else {
            Toast.makeText(
                    this,
                    "Device is not connected. Please, check your connection and try again.",
                    Toast.LENGTH_SHORT)
                    .show();
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (currentStream != null && newText.isEmpty()) {
            currentStream.shutdown();
        }
        return false;
    }

    /** Twitter4j Callbacks **/

    @Override
    public void onStatus(final Status status) {
        Log.v("MainActivity.onStatus", status.getText());
        statusList.add(status);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tweetListRecyclerView.getRecycledViewPool().clear();
                statusAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

    }

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

    }

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {

    }

    @Override
    public void onStallWarning(StallWarning warning) {

    }

    @Override
    public void onException(Exception ex) {

    }

}
