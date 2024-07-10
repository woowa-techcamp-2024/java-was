package codesquad.webserver.config.conatiner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleClassScanner implements ClassScanner {
    private static final Logger log = LoggerFactory.getLogger(SimpleClassScanner.class);

    @Override
    public Set<Class<?>> scanPackage(Class<?> mainClass) throws Exception {
        String basePackage = mainClass.getPackage().getName();
        return scanPackage(basePackage);
    }

    @Override
    public Set<Class<?>> scanPackage(String basePackage) throws Exception {
        log.debug("Scanning package: {}", basePackage);
        Set<Class<?>> classes = new HashSet<>();
        String path = basePackage.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("jar")) {
                processJarFile(resource, basePackage, classes);
            } else {
                processDirectory(new File(resource.getFile()), basePackage, classes);
            }
        }

        log.debug("Scanned {} classes in package: {}", classes.size(), basePackage);
        return classes;
    }

    private void processDirectory(File directory, String packageName, Set<Class<?>> classes) {
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    processDirectory(file, packageName + "." + file.getName(), classes);
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    addClass(classes, className);
                }
            }
        }
    }

    private void processJarFile(URL resource, String packageName, Set<Class<?>> classes) throws IOException {
        String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(packageName.replace('.', '/')) && entryName.endsWith(".class")) {
                    String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                    addClass(classes, className);
                }
            }
        }
    }

    private void addClass(Set<Class<?>> classes, String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (!clazz.isAnnotation()) {
                classes.add(clazz);
                log.debug("Added class: {}", className);
            }
        } catch (ClassNotFoundException e) {
            log.warn("Failed to load class: {}", className, e);
        }
    }
}