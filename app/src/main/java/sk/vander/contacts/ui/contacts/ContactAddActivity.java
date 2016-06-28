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
public class ContactAddActivity extends BaseActivity {
  public static final Uri URI = Uri.parse(HOST + "contact/add");
  @BindView(R.id.toolbar) Toolbar toolbar;

  @Override protected Object onCreateComponent(Object appComponent) {
    return DaggerContactAddActivityComponent.builder()
        .appComponent((AppComponent) appComponent)
        .build();
  }

  @Override protected void onInject() {
    DaggerService.<ContactAddActivityComponent>getDaggerComponent(this).inject(this);
  }

  @Override protected int layoutId() {
    return R.layout.activity_add_contact;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    ActivityScreen.setTransitionView(toolbar, "toolbar");
//    toolbar.setTitle(R.string.label_add_contact);
//    toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
//    toolbar.setNavigationOnClickListener(v -> finish());
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }
}