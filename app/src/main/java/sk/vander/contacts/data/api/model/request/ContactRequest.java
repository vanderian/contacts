package sk.vander.contacts.data.api.model.request;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 27/06/16.
 */
@AutoParcelGson
public abstract class ContactRequest {
  public abstract String name();
  public abstract String phone();

  public static ContactRequest create(String name, String phone) {
    return new AutoParcelGson_ContactRequest(name, phone);
  }
}
