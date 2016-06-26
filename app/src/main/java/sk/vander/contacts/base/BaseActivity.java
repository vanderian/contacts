package sk.vander.contacts.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by arashid on 26/06/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
  @Inject AppContainer appContainer;
  private ViewGroup content;
  private Object component;

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  @Override public Object getSystemService(String name) {
    return DaggerService.SERVICE_NAME.equals(name) ? component : super.getSystemService(name);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    component = onCreateComponent(DaggerService.getDaggerComponent(getApplicationContext()));
    onInject();

    if (appContainer == null) {
      throw new IllegalStateException("No injection happened. Add DaggerService.getDaggerComponent(this).inject(this) in onInject() implementation.");
    }

    initViewContainer();
  }

  protected void initViewContainer() {
    final LayoutInflater layoutInflater = getLayoutInflater();
    final ViewGroup container = appContainer.get(this);
    if (layoutId() != 0) {
      content = (ViewGroup) layoutInflater.inflate(layoutId(), container, false);
      container.addView(content);
      ButterKnife.bind(this);
    }
  }

  /**
   * <p>
   * Must be implemented by derived activities. Injection must be performed here.
   * Otherwise IllegalStateException will be thrown. Derived activity is
   * responsible to create and store it's component.
   * </p>
   *
   * @param appComponent application level component
   */
  protected abstract Object onCreateComponent(Object appComponent);
  protected abstract void onInject();

  protected abstract @LayoutRes int layoutId();

}
