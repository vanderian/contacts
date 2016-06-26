package sk.vander.contacts;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.ui.DebugUiModule;

/**
 * Created by arashid on 26/06/16.
 */
@ApplicationScope
@AutoComponent(
    modules = {DebugAppModule.class, DebugUiModule.class},
    superinterfaces = DebugAppGraph.class
)
@AutoInjector
public @interface BuildTypeAppComponent {
}
