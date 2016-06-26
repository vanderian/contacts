package sk.vander.contacts.data.api.model;

import org.jetbrains.annotations.Nullable;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 27/06/16.
 */
@AutoParcelGson
public abstract class Contact {
  public abstract String id();
  public abstract String name();
  public abstract String kind();
  @Nullable public abstract String phone();
  @Nullable public abstract String pictureUrl();
  @Nullable public abstract String etag();
}
