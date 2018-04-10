package pt.rhosystems.rhotwitter.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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

public class MainActivity extends AppCompatActivity implements StatusListener {

    private TwitterStreamFactory twitterStreamFactory;
    private StatusAdapter statusAdapter;
    private List<Status> statusList;
    private RecyclerView tweetListRecyclerView;
    private TwitterStream currentStream;

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

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder
                .setOAuthConsumerKey(getBaseContext().getString(R.string.consumer_key))
                .setOAuthConsumerSecret(getBaseContext().getString(R.string.consumer_secret))
                .setOAuthAccessToken(getBaseContext().getString(R.string.access_token))
                .setOAuthAccessTokenSecret(getBaseContext().getString(R.string.access_token_secret));

        Configuration configuration = configurationBuilder.build();

        twitterStreamFactory = new TwitterStreamFactory(configuration);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentStream = twitterStreamFactory.getInstance();
                currentStream.addListener(MainActivity.this);
                currentStream.filter(query);

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty() && currentStream != null) {
                    currentStream.shutdown();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    @Override
    public void onStatus(final Status status) {
        Log.v("MainActivity", status.getText());
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
