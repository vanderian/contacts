package sk.vander.contacts.base;


import sk.vander.contacts.base.navigation.activity.ActivityScreenSwitcher;

/**
 * Created by arashid on 24/06/16.
 */
public interface BaseGraph {
  AppContainer appContainer();
  ActivityHierarchyServer activityHierarchyServer();
  ActivityScreenSwitcher activityScreenSwitcher();
}
