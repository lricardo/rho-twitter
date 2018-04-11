package pt.rhosystems.rhotwitter.api;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface TwitterStreamingApi {
    @Headers({"User-Agent: RhoTwitter"})
    @Streaming
    @POST("statuses/filter.json")
    Call<ResponseBody> track(@Query("track") String terms);
}
