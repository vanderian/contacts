package sk.vander.contacts.ui.contacts.adapter;

import sk.vander.contacts.R;
import sk.vander.contacts.base.adapter.ListSource;
import sk.vander.contacts.data.api.model.Contact;

/**
 * Created by arashid on 27/06/16.
 */
public class ContactsSource extends ListSource<Contact> {
  @Override public int getViewType(int pos) {
    return 0;
  }

  @Override public int getLayoutRes(int viewType) {
    return R.layout.view_item_contact;
  }

  @Override public long getItemId(int position) {
    return get(position).hashCode();
  }
}
