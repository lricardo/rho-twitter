package pt.rhosystems.rhotwitter.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class TwitterApiHelper {

    public final TwitterStreamingApi api;

    private TwitterApiHelper () {
        String url = "https://stream.twitter.com/1.1/";

        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(
                "fnAknsxnKvRCAqDsetBHJyIF2",
                "GPYgsmYbarWiLITb0f9Q8jYad2nPvTDv1Xazyf8CTsYA6qr5V4"
        );

        consumer.setTokenWithSecret(
                "701711139604975616-v35PzBSGTtxqOZNiRvU6wShfhwIvpYS",
                "0Bu6WPpCLe4I4c27iIYrfWAQ836JFHyqjZwP3jc5colEU"
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
