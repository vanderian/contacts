package sk.vander.contacts.misc;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by arashid on 28/06/16.
 */
public final class Utils {
  private Utils() {};

  public static int getAnnotationValue(Class target, final Map<String, Integer> cache, Class<?> clazz) {
    Integer res = cache.get(getKey(target, clazz));
    if (res == null) {
      final Annotation annotation = target.getAnnotation(clazz);
      if (annotation != null) {
        try {
          final Method m = annotation.getClass().getDeclaredMethod("value", (Class[])null);
          res = (Integer) m.invoke(annotation, (Object[]) null);
          cache.put(getKey(target, clazz), res);
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
    return res != null ? res : 0;
  }

  private static String getKey(Class target, Class annotation) {
    return target + ":" + annotation;
  }
}
