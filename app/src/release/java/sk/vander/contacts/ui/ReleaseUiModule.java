package sk.vander.contacts.ui;

import dagger.Module;
import dagger.Provides;
import sk.vander.contacts.base.ActivityHierarchyServer;
import sk.vander.contacts.base.AppContainer;
import sk.vander.contacts.base.annotation.ActivityScreenSwitcherServer;
import sk.vander.contacts.base.annotation.ApplicationScope;

/**
 * Created by arashid on 26/06/16.
 */
@Module(includes = UiModule.class)
public class ReleaseUiModule {

  @Provides @ApplicationScope AppContainer providesAppContainer() {
    return AppContainer.DEFAULT;
  }

  @Provides @ApplicationScope
  ActivityHierarchyServer provideActivityHierarchyServer(@ActivityScreenSwitcherServer ActivityHierarchyServer server) {
    return server;
  }
}
