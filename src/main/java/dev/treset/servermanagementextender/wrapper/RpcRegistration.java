package dev.treset.servermanagementextender.wrapper;

import dev.treset.servermanagementextender.ServerManagementExtender;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class RpcRegistration {
    public static void applyRegister() {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(""))
                .setScanners(Scanners.TypesAnnotated));

        Set<Class<?>> res1 = reflections
                .getTypesAnnotatedWith(ServerManagementInitialized.class);
        List<Class<?>> classes = res1.stream()
                .map(clazz -> Map.entry(clazz.getAnnotation(ServerManagementInitialized.class).priority(), clazz))
                .sorted((e1,e2) -> e2.getKey() - e1.getKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        classes.forEach(c -> {
            try {
                // Initialize static final fields and call static constructor
                Class.forName(c.getName());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Failed to find RPC class: " + c.getName(), e);
            } catch (ExceptionInInitializerError e) {
                throw new IllegalStateException("Failed to initialize RPC class: " + c.getName(), e);
            }

            ServerManagementExtender.LOGGER.info("Registered RPC class: {}", c.getSimpleName());
        });
    }
}
