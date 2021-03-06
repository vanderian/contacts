package sk.vander.contacts;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import sk.vander.contacts.base.annotation.ApplicationScope;
import sk.vander.contacts.data.ReleaseDataModule;
import sk.vander.contacts.ui.ReleaseUiModule;

/**
 * Created by arashid on 26/06/16.
 */
@ApplicationScope
@AutoComponent(
    modules = {AppModule.class, ReleaseUiModule.class, ReleaseDataModule.class},
    superinterfaces = AppGraph.class
)
@AutoInjector
public @interface BuildTypeAppComponent {
}
