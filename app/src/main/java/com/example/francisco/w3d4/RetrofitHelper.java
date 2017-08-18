package com.example.francisco.w3d4;

import com.example.francisco.w3d4.model.Flickr;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by FRANCISCO on 17/08/2017.
 */

public class RetrofitHelper {
    public static final String BASE_URL = "http://api.flickr.com/";
    public static final String PATH = "services/feeds/photos_public.gne";
    public static final String QUERY_TAG = "kitten";
    public static final String QUERY_FORMAT = "json";
    public static final String QUERY_NOJSONCALLBACK = "1";

    public static Retrofit create(){

        //created a logging interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        //create a custom client to add the interceptor
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    //Receive user if we want to search a user here
    public static Call<Flickr> getFlickerCall(){

        Retrofit retrofit = create();
        FlickrService flickrService = retrofit.create(FlickrService.class);
        //return flickrService.get(user);
        return flickrService.getFlickerdata(QUERY_TAG, QUERY_FORMAT, QUERY_NOJSONCALLBACK);
    }

    public static Observable<Flickr> getWeatherObs(){
        Retrofit retrofit = create();
        FlickrService flickrService = retrofit.create(FlickrService.class);
        return flickrService.getFlickrObservable(QUERY_TAG, QUERY_FORMAT, QUERY_NOJSONCALLBACK);
    }

    public interface FlickrService{


        @GET("users/{user}/repos")
        Call<Flickr> get(@Path("user") String user);

        @GET(PATH)
        //@Header()
        Call<Flickr> getFlickerdata(@Query("tag") String tagcode, @Query("format") String format, @Query("nojsoncallback") String nojsoncallback);

        @GET(PATH)
        Observable<Flickr> getFlickrObservable(@Query("tag") String tagcode, @Query("format") String format, @Query("nojsoncallback") String nojsoncallback);

    }
}
