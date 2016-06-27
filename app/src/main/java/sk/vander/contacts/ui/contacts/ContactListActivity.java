package sk.vander.contacts.ui.contacts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import sk.vander.contacts.AppComponent;
import sk.vander.contacts.R;
import sk.vander.contacts.base.BaseActivity;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.StandardActivity;

@StandardActivity
public class ContactListActivity extends BaseActivity {
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.fab) FloatingActionButton fab;

  @Override protected Object onCreateComponent(Object appComponent) {
    return DaggerContactListActivityComponent.builder()
        .appComponent((AppComponent) appComponent)
        .build();
  }

  @Override protected void onInject() {
    DaggerService.<ContactListActivityComponent>getDaggerComponent(this).inject(this);
  }

  @Override protected int layoutId() {
    return R.layout.activity_main;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(toolbar);
    fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null).show());
  }
}