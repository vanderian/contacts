package sk.vander.contacts.base;

import autodagger.AutoComponent;
import autodagger.AutoInjector;
import sk.vander.contacts.App;
import sk.vander.contacts.base.annotation.ActivityScope;

/**
 * Created by arashid on 26/06/16.
 */
@AutoComponent(
    dependencies = App.class,
    modules = ActivityModule.class
)
@AutoInjector
@ActivityScope
public @interface StandardActivity {
}
