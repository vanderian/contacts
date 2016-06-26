package sk.vander.contacts.base;

import javax.inject.Scope;

@Scope
public @interface DaggerScope {
    Class<?> value();
}
