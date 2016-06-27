package sk.vander.contacts.data.api.model;

import java.util.List;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 27/06/16.
 */
@AutoParcelGson
public abstract class ResponseContacts extends ResponseList<Contact> {
  public abstract List<Contact> items();
}
