package id.tech;

import id.tech.model.PojoAbsence;
import id.tech.model.PojoLogin;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by macbook on 5/9/16.
 */
public interface REST {

    @FormUrlEncoded
    @POST("Login?")
    Observable<PojoLogin> login(
            @Field("Username")String username,
            @Field("Password")String password
    );

    @FormUrlEncoded
    @POST("Absence?")
    Observable<PojoAbsence> absence_others(
            @Field("Username")String Username,
            @Field("StoreID")String StoreID,
            @Field("Absence")String Absence,
            @Field("Latitude")String Latitude,
            @Field("Longitude")String Longitude,
            @Field("Remarks")String Remarks
    );

    @FormUrlEncoded
    @POST("Absence?")
    Observable<PojoAbsence> absence_inout(
            @Field("Username")String Username,
            @Field("StoreID")String StoreID,
            @Field("Absence")String Absence,
            @Field("Latitude")String Latitude,
            @Field("Longitude")String Longitude
    );
}
