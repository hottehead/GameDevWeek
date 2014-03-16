package de.hochschuletrier.gdw.commons.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for finding classes
 */
public class ClassUtils {
    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * Scans all classes accessible from the context class loader which belong
     * to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Set<Class> findClassesInPackage(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        HashSet<Class> classes = new HashSet<Class>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();

            if (resource.getProtocol().equals("file")) {
                try {
                    findClassesInDir(new File(resource.toURI()), packageName, classes);
                } catch (URISyntaxException e) {
                    logger.error("Failed finding classes in package", e);
                }
            } else if (resource.getProtocol().equals("jar")) {
                findClassesInJar(resource, path, classes);
            }
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirectories.
     *
     * @param directory The base directory
     * @param packageName The package name for classes found inside the base directory
     * @param classes The classes
     * @throws ClassNotFoundException
     */
    private static void findClassesInDir(File directory, String packageName, HashSet<Class> classes) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    findClassesInDir(file, packageName + "." + file.getName(), classes);
                } else if (file.getName().endsWith(".class")) {
                    try {
                        classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                    } catch (ClassNotFoundException ex) {
                    }
                }
            }
        }
    }

    /**
     * Method to find all classes in a jar file
     *
     * @param jarURL the jar url
     * @param packageName The package name for classes found inside the base directory
     * @param classes The classes
     */
    private static void findClassesInJar(URL jarURL, String packageName, HashSet<Class> classes) {
        try {
            String jarPath = jarURL.getPath().substring(5, jarURL.getPath().indexOf("!"));
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(packageName) && name.endsWith(".class")) {
                    String className = name.substring(0, name.length() - 6).replace('/', '.');
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException ex) {
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed finding classes in jar", e);
        }
    }
}
