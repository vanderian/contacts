package sk.vander.contacts.data.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.base.prefs.BooleanPreference;
import sk.vander.contacts.data.api.mock.ContactServiceMock;
import sk.vander.contacts.data.api.mock.OrderServiceMock;
import sk.vander.contacts.data.api.service.ContactService;
import sk.vander.contacts.data.api.service.OrderService;

/**
 * Created by arashid on 27/06/16.
 */
@Module(includes = ApiModule.class)
public class DebugApiModule {

  @Provides @ApplicationScope @MockMode Boolean provideMockMode(@MockMode BooleanPreference mockPref) {
    return mockPref.get();
  }

  @Provides @ApplicationScope HttpLoggingInterceptor.Level provideLoggingLevel() {
    return HttpLoggingInterceptor.Level.BODY;
  }

  @ApplicationScope @Provides HttpLoggingInterceptor provideLoggingInterceptor(HttpLoggingInterceptor.Level level) {
    final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(level);
    return interceptor;
  }

  @ApplicationScope @Provides OkHttpClient provideOkHttpClient(Cache cache,
                                                               HttpLoggingInterceptor loggingInterceptor,
                                                               @ForceCacheInterceptor Interceptor forceCache) {
    return new OkHttpClient.Builder()
        .addNetworkInterceptor(new StethoInterceptor())
        .addNetworkInterceptor(forceCache)
        .addInterceptor(loggingInterceptor)
        .cache(cache)
        .build();
  }

  @ApplicationScope @Provides NetworkBehavior provideNetworkBehavior() {
    return NetworkBehavior.create();
  }

  @ApplicationScope @Provides MockRetrofit provideMockRetrofit(Retrofit retrofit, NetworkBehavior networkBehavior) {
    return new MockRetrofit.Builder(retrofit).networkBehavior(networkBehavior).build();
  }

  @ApplicationScope @Provides ContactService provideContactService(Retrofit retrofit, MockRetrofit mockRetrofit, @MockMode Boolean isMock) {
    return isMock ? new ContactServiceMock(mockRetrofit.create(ContactService.class)) : retrofit.create(ContactService.class);
  }

  @ApplicationScope @Provides OrderService provideOrderService(Retrofit retrofit, MockRetrofit mockRetrofit, @MockMode Boolean isMock) {
    return isMock ? new OrderServiceMock(mockRetrofit.create(OrderService.class)) : retrofit.create(OrderService.class);
  }
}
