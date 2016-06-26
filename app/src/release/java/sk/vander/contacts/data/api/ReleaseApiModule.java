package sk.vander.contacts.data.api;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import sk.vander.contacts.base.annotation.ApplicationScope;

/**
 * Created by arashid on 27/06/16.
 */
@Module(includes = ApiModule.class)
public class ReleaseApiModule {
  @Provides @ApplicationScope OkHttpClient provideOkHttpClient() {
    return new OkHttpClient.Builder().build();
  }
}
