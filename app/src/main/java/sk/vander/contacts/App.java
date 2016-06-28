package sk.vander.contacts;

import android.support.annotation.CallSuper;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import sk.vander.contacts.base.BaseApp;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by arashid on 26/06/16.
 */
@BuildTypeAppComponent
public class App extends BaseApp<AppComponent> {
  @Inject Picasso picasso;

  @Override protected void buildComponentAndInject() {
    component = Initializer.init(this);
    component.inject(this);
  }

  @CallSuper @Override protected void init() {
    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        .build());

    Picasso.setSingletonInstance(picasso);
  }
}
