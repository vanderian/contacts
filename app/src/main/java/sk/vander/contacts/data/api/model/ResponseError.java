package sk.vander.contacts.data.api.model;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import auto.parcelgson.AutoParcelGson;

/**
 * Created by arashid on 27/06/16.
 */
@AutoParcelGson
public abstract class ResponseError {
  public abstract Error error();

  @AutoParcelGson
  public static abstract class Error {
    public abstract List<ErrorDesc> errors();
    public abstract int code();
    public abstract String message();
  }

  @AutoParcelGson
  public static abstract class ErrorDesc {
    public abstract String domain();
    public abstract String reason();
    public abstract String message();
    @Nullable public abstract String locationType();
    @Nullable public abstract String location();
  }
}
