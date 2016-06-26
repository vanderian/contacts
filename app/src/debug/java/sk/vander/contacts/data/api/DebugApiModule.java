package sk.vander.contacts.data.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import sk.vander.contacts.base.annotation.ApplicationScope;

/**
 * Created by arashid on 27/06/16.
 */
@Module(includes = ApiModule.class)
public class DebugApiModule {

  @Provides @ApplicationScope HttpLoggingInterceptor.Level provideLoggingLevel() {
    return HttpLoggingInterceptor.Level.BODY;
  }

  @ApplicationScope @Provides HttpLoggingInterceptor provideLoggingInterceptor(HttpLoggingInterceptor.Level level) {
    final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(level);
    return interceptor;
  }

  @ApplicationScope @Provides OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
    return new OkHttpClient.Builder()
        .addNetworkInterceptor(new StethoInterceptor())
        .addInterceptor(loggingInterceptor)
        .build();
  }
}
