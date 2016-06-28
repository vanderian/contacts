package sk.vander.contacts.ui.contacts;

import autodagger.AutoInjector;
import sk.vander.contacts.AppComponent;
import sk.vander.contacts.R;
import sk.vander.contacts.base.BaseActivity;
import sk.vander.contacts.base.DaggerService;
import sk.vander.contacts.base.StandardActivity;
import sk.vander.contacts.base.annotation.LayoutId;
import sk.vander.contacts.base.annotation.ScreenLabel;

@StandardActivity
@AutoInjector
@ScreenLabel(R.string.label_contacts)
@LayoutId(R.layout.activity_contacts)
public class ContactListActivity extends BaseActivity {

  @Override protected Object onCreateComponent(Object appComponent) {
    return DaggerContactListActivityComponent.builder()
        .appComponent((AppComponent) appComponent)
        .build();
  }

  @Override protected void onInject() {
    DaggerService.<ContactListActivityComponent>getDaggerComponent(this).inject(this);
  }
}