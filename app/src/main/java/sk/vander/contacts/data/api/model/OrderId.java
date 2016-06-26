package sk.vander.contacts.data.api.model;

import org.jetbrains.annotations.Nullable;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 27/06/16.
 */
@AutoParcelGson
public abstract class OrderId {
  public abstract String kind();
  public abstract String appId();
  public abstract String id();
  public abstract Boolean complete();
  @Nullable public abstract OrderId parent();
}
