package sk.vander.contacts;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by arashid on 21/06/16.
 */
public class App extends Application {
  private AppComponent appComponent;
  @Inject ActivityHierarchyServer activityHierarchyServer;

  @Override public void onCreate() {
    super.onCreate();

    Timber.plant(new Timber.DebugTree());

    appComponent = AppComponent.Initializer.init(this);
    appComponent.inject(this);

    registerActivityLifecycleCallbacks(activityHierarchyServer);
  }

  public static AppComponent getComponent(Context context) {
    return ((App) context.getApplicationContext()).appComponent;
  }
}
