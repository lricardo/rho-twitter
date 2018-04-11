package pt.rhosystems.rhotwitter.api;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import pt.rhosystems.rhotwitter.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class TwitterStreamingApiHelper {

    public final TwitterStreamingApi api;

    public TwitterStreamingApiHelper(Context context) {
        String url = "https://stream.twitter.com/1.1/";

        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(
                context.getString(R.string.consumer_key),
                context.getString(R.string.consumer_secret)
        );

        consumer.setTokenWithSecret(
                context.getString(R.string.access_token),
                context.getString(R.string.access_token_secret)
        );

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS)
                .addInterceptor(new SigningInterceptor(consumer))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        api = retrofit.create(TwitterStreamingApi.class);
    }
}
