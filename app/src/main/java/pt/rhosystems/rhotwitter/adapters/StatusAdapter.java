package pt.rhosystems.rhotwitter.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import pt.rhosystems.rhotwitter.R;
import twitter4j.Status;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private Context context;
    private List<Status> statusList;

    public StatusAdapter(Context context, List<Status> statusList) {
        this.context = context;
        this.statusList = statusList;
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
        Status status = statusList.get(position);

        TextView tweetText = holder.tweetText;
        TextView tweetUserFullName = holder.tweetUserFullName;
        TextView tweetUsername = holder.tweetUsername;
        TextView tweetDate = holder.tweetDate;

        tweetText.setText(status.getText());
        tweetUserFullName.setText(status.getUser().getName());
        tweetUsername.setText(status.getUser().getScreenName());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy h:mm a");
        tweetDate.setText(sdf.format(status.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public void updateStatusList (List<Status> statusList) {
        this.statusList = statusList;
        notifyItemRangeChanged(0, statusList.size());
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
