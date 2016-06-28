package sk.vander.contacts.ui.contacts;

import android.net.Uri;
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
import sk.vander.contacts.data.provider.DataProvider;

@StandardActivity
@AutoInjector
public class ContactOrderActivity extends BaseActivity {
  public static final Uri URI = Uri.parse(HOST + "contact/order");
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

  @Override protected int layoutId() {
    return R.layout.activity_orders;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setTitle(dataProvider.selectedContact().getValue().name());
  }
}