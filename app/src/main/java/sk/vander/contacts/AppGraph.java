package sk.vander.contacts;


import sk.vander.contacts.navigation.activity.ActivityScreenSwitcher;

/**
 * Created by arashid on 24/06/16.
 */
public interface AppGraph {
  void inject(App app);

  ActivityHierarchyServer activityHierarchyServer();
  ActivityScreenSwitcher activityScreenSwitcher();
}
