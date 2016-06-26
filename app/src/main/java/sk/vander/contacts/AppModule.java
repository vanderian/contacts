package sk.vander.contacts;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import autodagger.AutoExpose;
import dagger.Module;
import dagger.Provides;
import sk.vander.contacts.base.BaseAppModule;
import sk.vander.contacts.base.annotation.ApplicationScope;

/**
 * Created by arashid on 26/06/16.
 */
@Module(includes = BaseAppModule.class)
public class AppModule {

  @AutoExpose(App.class)
  @Provides @ApplicationScope SharedPreferences provideSharedPreferences(Application application) {
    return application.getSharedPreferences("name", Context.MODE_PRIVATE);
  }
}
