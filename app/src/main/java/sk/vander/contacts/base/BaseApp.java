package sk.vander.contacts.base;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

/**
 * Created by arashid on 21/06/16.
 */
public abstract class BaseApp<Component extends BaseGraph> extends Application {
  protected Component component;
  @Inject ActivityHierarchyServer activityHierarchyServer;

  @Override public void onCreate() {
    super.onCreate();

    buildComponentAndInject();

    registerActivityLifecycleCallbacks(activityHierarchyServer);

    init();
  }

  @Override public Object getSystemService(String name) {
    return DaggerService.SERVICE_NAME.equals(name) ? component : super.getSystemService(name);
  }

  public static BaseApp get(Context context) {
    return (BaseApp) context.getApplicationContext();
  }

  protected abstract void buildComponentAndInject();
  protected abstract void init();
}
