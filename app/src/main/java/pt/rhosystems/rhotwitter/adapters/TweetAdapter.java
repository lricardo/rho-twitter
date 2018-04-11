package pt.rhosystems.rhotwitter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.List;

import pt.rhosystems.rhotwitter.R;
import pt.rhosystems.rhotwitter.models.Tweet;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private Context context;
    private List<Tweet> tweetList;

    public TweetAdapter(Context context, List<Tweet> tweetList) {
        this.context = context;
        this.tweetList = tweetList;
    }

    private Context getContext() {
        return this.context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);

        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweetList.get(holder.getAdapterPosition());

        TextView tweetText = holder.tweetText;
        TextView tweetUserFullName = holder.tweetUserFullName;
        TextView tweetUsername = holder.tweetUsername;
        TextView tweetDate = holder.tweetDate;

        tweetText.setText(tweet.getText());
        tweetUserFullName.setText(tweet.getUser().getName());
        tweetUsername.setText(tweet.getUser().getScreenName());
        //SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy h:mm a");
        //tweetDate.setText(sdf.format(tweet.getCreatedAt()));
        tweetDate.setText(tweet.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tweetText;
        public TextView tweetUserFullName;
        public TextView tweetUsername;
        public TextView tweetDate;


        public ViewHolder(View itemView) {
            super(itemView);

            tweetText = (TextView) itemView.findViewById(R.id.tweetText);
            tweetUserFullName = (TextView) itemView.findViewById(R.id.tweetCardFullName);
            tweetUsername = (TextView) itemView.findViewById(R.id.tweetCardUsername);
            tweetDate = (TextView) itemView.findViewById(R.id.tweetCardDate);

        }
    }
}
