package in.dete.oops;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("payment")
    Call<JsonObject> genCheckSum(@Body JsonObject obj);

}
