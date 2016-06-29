package sk.vander.contacts.data.api;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.data.api.service.ContactService;
import sk.vander.contacts.data.api.service.OrderService;

/**
 * Created by arashid on 27/06/16.
 */
@Module(includes = ApiModule.class)
public class ReleaseApiModule {
  @Provides @ApplicationScope OkHttpClient provideOkHttpClient(Cache cache, @ForceCacheInterceptor Interceptor forceCache) {
    return new OkHttpClient.Builder()
        .addNetworkInterceptor(forceCache)
        .cache(cache)
        .build();
  }

  @ApplicationScope @Provides ContactService provideContactService(Retrofit retrofit) {
    return retrofit.create(ContactService.class);
  }

  @ApplicationScope @Provides OrderService provideOrderService(Retrofit retrofit) {
    return retrofit.create(OrderService.class);
  }
}
