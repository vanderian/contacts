package sk.vander.contacts.ui.contacts;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import autodagger.AutoInjector;
import butterknife.BindView;
import sk.vander.contacts.AppComponent;
import sk.vander.contacts.R;
import sk.vander.contacts.base.BaseActivity;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.StandardActivity;

@StandardActivity
@AutoInjector
public class ContactListActivity extends BaseActivity {
  public static final Uri URI = Uri.parse(HOST + "contact/list");
  @BindView(R.id.toolbar) Toolbar toolbar;

  @Override protected Object onCreateComponent(Object appComponent) {
    return DaggerContactListActivityComponent.builder()
        .appComponent((AppComponent) appComponent)
        .build();
  }

  @Override protected void onInject() {
    DaggerService.<ContactListActivityComponent>getDaggerComponent(this).inject(this);
  }

  @Override protected int layoutId() {
    return R.layout.activity_contacts;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    toolbar.setTitle(R.string.label_contacts);
    setSupportActionBar(toolbar);
  }
}