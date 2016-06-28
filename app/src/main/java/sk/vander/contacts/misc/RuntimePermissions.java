package sk.vander.contacts.misc;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arashid on 28/06/16.
 */
public final class RuntimePermissions {
  private RuntimePermissions(){}

  /**
   * Helper for using permissions request for Android 6+
   * Each permission will be checked if it is already granted, and if not it will be requested
   * by system dialog
   *
   * @param activity Actvity
   * @param permissions needed permissions
   */
  public static boolean requestPermissions(Activity activity, String... permissions) {
    List<String> neededPermissions = new ArrayList<>();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      for (String permission : permissions) {
        if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
          neededPermissions.add(permission);
        }
      }

      if (!neededPermissions.isEmpty()) {
        activity.requestPermissions(neededPermissions.toArray(new String[neededPermissions.size()]), 0);
      }
    }
    return neededPermissions.isEmpty();
  }
}
