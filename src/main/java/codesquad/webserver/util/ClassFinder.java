package codesquad.webserver.util;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassFinder {
    private static Logger log = LoggerFactory.getLogger(ClassFinder.class);

    private ClassFinder() {
    }

    public static List<Class<?>> findAllClass(String basePackage) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = basePackage.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                log.info("resource: "+resource.getFile());
                classes.addAll(findClasses(resource, basePackage));
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return classes;
    }

    private static List<Class<?>> findClasses(URL resource, String basePackage)
            throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();

        String protocol = resource.getProtocol();
        if ("file".equals(protocol)) {
            classes.addAll(findClassesFromDirectory(new File(resource.getFile()), basePackage));
        }
        else if ("jar".equals(protocol)) {
            classes.addAll(findClassesFromJar(resource, basePackage));
        }

        return classes;
    }

    private static List<Class<?>> findClassesFromDirectory(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClassesFromDirectory(file, packageName.isEmpty() ? file.getName() : packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName.isEmpty() ?
                        file.getName().substring(0, file.getName().length() - 6) :
                        packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }

    private static List<Class<?>> findClassesFromJar(URL resource, String basePackage)
            throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            String basePath = basePackage.replace('.', '/');
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(basePath) && entryName.endsWith(".class")) {
                    String className = entryName.substring(0, entryName.length() - 6).replace('/', '.');
                    if(isValidClassName(className)){
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        return classes;
    }

    private static boolean isValidClassName(String className) {
        return !className.startsWith("META-INF") && !className.equals("module-info");
    }
}
