package sk.vander.contacts.data.api.mock;

import java.util.Arrays;

import retrofit2.http.Body;
import retrofit2.mock.BehaviorDelegate;
import rx.Observable;
import sk.vander.contacts.data.api.model.Contact;
import sk.vander.contacts.data.api.model.request.ContactRequest;
import sk.vander.contacts.data.api.model.response.ContactsResponse;
import sk.vander.contacts.data.api.service.ContactService;

/**
 * Created by arashid on 28/06/16.
 */
public class ContactServiceMock implements ContactService {
  private final BehaviorDelegate<ContactService> delegate;

  public ContactServiceMock(BehaviorDelegate<ContactService> delegate) {
    this.delegate = delegate;
  }

  @Override public Observable<ContactsResponse> getContactsCached() {
    return getContacts();
  }

  @Override public Observable<ContactsResponse> getContacts() {
    final Contact c1 = Contact.create("id_c1", "jano", "item", "87687678", "profile1.jpg", "asd89");
    final Contact c2 = Contact.create("id_c2", "ste4evo", "item", "845347687678", "", "asd89");
    final Contact c3 = Contact.create("id_c3", "c3p0", "item", "346346346", null, "asd89");
    final ContactsResponse rc = ContactsResponse.create("list", "etag", Arrays.asList(c1, c2, c3, c1, c2, c3, c1, c2, c3));
    return delegate.returningResponse(rc).getContacts();
  }

  @Override public Observable<Contact> createContact(@Body ContactRequest contact) {
    final Contact c = Contact.create("id_new_contact", contact.name(), "item", contact.phone(), null, null);
    return delegate.returningResponse(c).createContact(contact);
  }
}
