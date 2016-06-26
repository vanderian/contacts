package sk.vander.contacts.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import auto.parcelgson.gson.AutoParcelGsonTypeAdapterFactory;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import sk.vander.contacts.base.annotation.ApplicationScope;

/**
 * Created by arashid on 27/06/16.
 */
@Module
public class ApiModule {
  private static final String ENDPOINT = "http://papagaj-breweria.herokuapp.com/api/v1/menu/54ca39f401731406200082df/";
  
  @ApplicationScope @Provides Gson provideGson() {
    return new GsonBuilder().registerTypeAdapterFactory(new AutoParcelGsonTypeAdapterFactory()).create();
  }

  @ApplicationScope @Provides Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
    return new Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();
  }
}
