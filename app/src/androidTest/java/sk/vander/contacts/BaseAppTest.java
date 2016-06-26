package sk.vander.contacts;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class BaseAppTest extends ApplicationTestCase<Application> {
  public BaseAppTest() {
    super(Application.class);
  }
}