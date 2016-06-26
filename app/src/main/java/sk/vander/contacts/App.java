package sk.vander.contacts;

import android.support.annotation.CallSuper;

import sk.vander.contacts.base.BaseApp;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by arashid on 26/06/16.
 */
@BuildTypeAppComponent
public class App extends BaseApp<AppComponent> {
  @Override protected void buildComponentAndInject() {
    component = Initializer.init(this);
    component.inject(this);
  }

  @CallSuper @Override protected void init() {
    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .build());
  }
}
