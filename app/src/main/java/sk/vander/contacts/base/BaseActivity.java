package sk.vander.contacts.base;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.vander.contacts.R;
import sk.vander.contacts.base.annotation.HideToolbar;
import sk.vander.contacts.base.annotation.LayoutId;
import sk.vander.contacts.base.annotation.ScreenLabel;
import sk.vander.contacts.base.annotation.ShowUp;
import sk.vander.contacts.base.navigation.activity.ActivityScreenSwitcher;
import sk.vander.contacts.base.navigation.activity.ActivityUriScreen;
import sk.vander.contacts.misc.Utils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by arashid on 26/06/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
  private static final Map<String, Integer> INT_CACHE = new LinkedHashMap<>();

  @Inject protected ActivityScreenSwitcher screenSwitcher;
  @Inject AppContainer appContainer;

  private Object component;

  @BindView(R.id.frame_content) protected ViewGroup contentFrame;
  @BindView(R.id.appBar) protected AppBarLayout appBar;
  @BindView(R.id.toolbar) protected Toolbar toolbar;
  @BindView(R.id.progress) protected View progress;

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
    setupAppBar();
  }

  @Override public void setTitle(CharSequence title) {
    toolbar.setTitle(title);
  }

  @Override public void setTitle(int titleId) {
    toolbar.setTitle(titleId);
  }

  protected void initViewContainer() {
    final LayoutInflater layoutInflater = getLayoutInflater();
    final ViewGroup container = appContainer.get(this);
    layoutInflater.inflate(R.layout.activity_base, container, true);
    contentFrame = ButterKnife.findById(this, R.id.frame_content);
    if (layoutId() != 0) {
      layoutInflater.inflate(layoutId(), contentFrame, true);
//      content = (ViewGroup) layoutInflater.inflate(layoutId(), container, false);
//      contentFrame.addView(content);
    }
    ButterKnife.bind(this);
//    consume clicks
    progress.setOnClickListener(v -> {});
  }

  protected void setupAppBar() {
    appBar.setVisibility(getClass().isAnnotationPresent(HideToolbar.class) ? View.GONE : View.VISIBLE);
    final int titleRes = Utils.getAnnotationValue(getClass(), INT_CACHE, ScreenLabel.class);
    if (titleRes != 0) toolbar.setTitle(titleRes);
    if (getClass().isAnnotationPresent(ShowUp.class)) {
      final int resId = Utils.getAnnotationValue(getClass(), INT_CACHE, ShowUp.class);
      toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
      toolbar.setNavigationOnClickListener(v -> {
        if (resId != 0) {
          screenSwitcher.open(ActivityUriScreen.withUri(Uri.parse(getString(resId))), true);
        } else {
          onBackPressed();
        }
      });
    }
  }

  protected @LayoutRes int layoutId() {
    return Utils.getAnnotationValue(getClass(), INT_CACHE, LayoutId.class);
  }

  public AppBarLayout getAppBar() {
    return appBar;
  }

  public void showProgress(boolean show) {
    progress.setVisibility(show ? View.VISIBLE : View.GONE);
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
}
