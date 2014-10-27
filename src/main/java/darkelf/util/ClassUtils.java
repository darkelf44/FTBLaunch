package darkelf.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * Created by evan on 10/26/14.
 */
public class ClassUtils {

    public static Object newInstance(String type) {
        try {
            return newInstance(Object.class.forName(type, true, Thread.currentThread().getContextClassLoader()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Convenience method to quickly create an instance of a class, using the default constructor.
     *
     * @param type The class to instantiate
     * @return And instance of the class
     */
    public static Object newInstance(Class type) {
        try {
            return type.getConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * Accessing resources in the JVM is tied to locating classes. When you can't choose a class that is sure to be
     * located at the same location as the resource you're trying to load, the the thread context class loader is a
     * relatively safe choice.
     *
     * @param name path to the resource to load
     * @return the URL representing the resource
     */
    public static final URL getResource(String name) {
        return getResource(name, null);
    }

    /**
     * When you do have a class to be used for class loading, this function will use it. Should the class object be a
     * null reference, the function defaults to using the thread context class loader.
     *
     * @param name path to the resource to load
     * @param clazz the class with the @c ClassLoader to use
     * @return the URL representing the resource
     */
    public static final URL getResource(String name, Class<?> clazz) {
        // remove optional '/' prefix TODO: remove this when no longer used
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        // load class
        if (clazz == null) {
            return Thread.currentThread().getContextClassLoader().getResource(name);
        } else {
            return clazz.getClassLoader().getResource(name);
        }
    }

}
