package sk.vander.contacts.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import auto.parcelgson.gson.AutoParcelGsonTypeAdapterFactory;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.data.api.error.RxErrorHandlingCallAdapterFactory;

/**
 * Created by arashid on 27/06/16.
 */
@Module
public class ApiModule {
  public static final String HOST = "https://inloop-contacts.appspot.com/";
  private static final String API = "_ah/api/";

  @ApplicationScope @Provides Gson provideGson() {
    return new GsonBuilder().registerTypeAdapterFactory(new AutoParcelGsonTypeAdapterFactory()).create();
  }

  @ApplicationScope @Provides Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
        .baseUrl(HOST + API)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
//        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
        .build();
  }
}