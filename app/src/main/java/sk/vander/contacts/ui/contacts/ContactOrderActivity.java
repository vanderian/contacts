package sk.vander.contacts.ui.contacts;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import autodagger.AutoInjector;
import butterknife.BindView;
import sk.vander.contacts.AppComponent;
import sk.vander.contacts.R;
import sk.vander.contacts.base.BaseActivity;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.StandardActivity;
import sk.vander.contacts.base.annotation.LayoutId;
import sk.vander.contacts.base.annotation.ShowUp;
import sk.vander.contacts.base.navigation.activity.ActivityScreen;
import sk.vander.contacts.data.provider.DataProvider;

@StandardActivity
@AutoInjector
@ShowUp
@LayoutId(R.layout.activity_orders)
public class ContactOrderActivity extends BaseActivity {
  @Inject DataProvider dataProvider;
  @BindView(R.id.toolbar) Toolbar toolbar;

  @Override protected Object onCreateComponent(Object appComponent) {
    return DaggerContactOrderActivityComponent.builder()
        .appComponent((AppComponent) appComponent)
        .build();
  }

  @Override protected void onInject() {
    DaggerService.<ContactOrderActivityComponent>getDaggerComponent(this).inject(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityScreen.setTransitionView(appBar, "appBar");
    setTitle(dataProvider.selectedContact().getValue().name());
  }
}