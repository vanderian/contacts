package sk.vander.contacts.data.api.model.response;

import java.util.List;

import auto.parcelgson.AutoParcelGson;
import sk.vander.contacts.data.api.model.Contact;

/**
 * Created by arashid on 27/06/16.
 */
@AutoParcelGson
public abstract class ContactsResponse extends ListResponse<Contact> {
  public abstract List<Contact> items();

  public static ContactsResponse create(String kind, String etag, List<Contact> items) {
    return new AutoParcelGson_ContactsResponse(kind, etag, items);
  }
}
