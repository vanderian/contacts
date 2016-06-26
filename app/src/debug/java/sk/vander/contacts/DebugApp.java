package sk.vander.contacts;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import autodagger.AutoInjector;
import timber.log.Timber;

/**
 * Created by arashid on 26/06/16.
 */
@AutoInjector(App.class)
public class DebugApp extends App {
  @Override protected void buildComponentAndInject() {
    super.buildComponentAndInject();
    component.inject(this);
  }

  @Override protected void init() {
    super.init();
    Timber.plant(new Timber.DebugTree());
    LeakCanary.install(this);
    Stetho.initializeWithDefaults(this);
  }
}
