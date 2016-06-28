package sk.vander.contacts.ui.contacts;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import autodagger.AutoInjector;
import butterknife.BindView;
import sk.vander.contacts.AppComponent;
import sk.vander.contacts.R;
import sk.vander.contacts.base.BaseActivity;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.StandardActivity;
import sk.vander.contacts.base.annotation.LayoutId;
import sk.vander.contacts.base.annotation.ScreenLabel;
import sk.vander.contacts.base.annotation.ShowUp;

@StandardActivity
@AutoInjector
@ShowUp
@ScreenLabel(R.string.label_add_contact)
@LayoutId(R.layout.activity_add_contact)
public class ContactAddActivity extends BaseActivity {
  @BindView(R.id.toolbar) Toolbar toolbar;

  @Override protected Object onCreateComponent(Object appComponent) {
    return DaggerContactAddActivityComponent.builder()
        .appComponent((AppComponent) appComponent)
        .build();
  }

  @Override protected void onInject() {
    DaggerService.<ContactAddActivityComponent>getDaggerComponent(this).inject(this);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
//    ActivityScreen.setTransitionView(appBar, "toolbar");
  }
}