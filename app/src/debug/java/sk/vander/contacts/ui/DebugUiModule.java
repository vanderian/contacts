package sk.vander.contacts.ui;

import dagger.Module;
import dagger.Provides;
import sk.vander.contacts.base.ActivityHierarchyServer;
import sk.vander.contacts.base.AppContainer;
import sk.vander.contacts.base.SocketActivityHierarchyServer;
import sk.vander.contacts.base.annotation.ActivityScreenSwitcherServer;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.ui.debug.DebugAppContainer;

/**
 * Created by arashid on 26/06/16.
 */
@Module(includes = UiModule.class)
public class DebugUiModule {

  @Provides @ApplicationScope AppContainer providesAppContainer(DebugAppContainer container) {
    return container;
  }

  @Provides @ApplicationScope
  ActivityHierarchyServer provideActivityHierarchyServer(@ActivityScreenSwitcherServer ActivityHierarchyServer server) {
    final ActivityHierarchyServer.Proxy proxy = new ActivityHierarchyServer.Proxy();
    proxy.addServer(server);
    proxy.addServer(new SocketActivityHierarchyServer());
    return proxy;
  }
}
