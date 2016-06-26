package sk.vander.contacts.base;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import sk.vander.contacts.base.annotation.ApplicationScope;

/**
 * Created by vander on 4/17/15.
 */
@Module
public class BaseAppModule {
  private final Application app;

  public BaseAppModule(Application app) {
    this.app = app;
  }

  @Provides @ApplicationScope Application provideApplication() {
    return app;
  }
}
