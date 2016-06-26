package sk.vander.contacts;

import sk.vander.contacts.base.BaseAppModule;

/**
 * Created by arashid on 26/06/16.
 */
public final class Initializer {
  static AppComponent init(App app) {
    return DaggerAppComponent.builder()
        .baseAppModule(new BaseAppModule(app))
        .build();
  }

  private Initializer() {
  } // No instances.
}
