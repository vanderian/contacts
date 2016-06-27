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
import sk.vander.contacts.data.api.service.ContactService;
import sk.vander.contacts.data.api.service.OrderService;

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

  //// TODO: 27/06/16 debug/mock and release impl
  @ApplicationScope @Provides ContactService provideContacService(Retrofit retrofit) {
    return retrofit.create(ContactService.class);
  }

  //// TODO: 27/06/16 debug/mock and release impl
  @ApplicationScope @Provides OrderService provideOrderService(Retrofit retrofit) {
    return retrofit.create(OrderService.class);
  }
}
