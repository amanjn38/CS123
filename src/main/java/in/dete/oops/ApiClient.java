package in.dete.oops;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pratd on 01-04-2018.
 */
public class ApiClient {
    public static final String baseurl = "https://us-central1-oops-9e2b0.cloudfunctions.net/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder().baseUrl(baseurl).client(client).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}

