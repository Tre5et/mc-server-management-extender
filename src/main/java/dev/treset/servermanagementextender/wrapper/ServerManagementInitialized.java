package dev.treset.servermanagementextender.wrapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class to be initialized with the RPC methods.
 * <p>
 * All {@code public static} fields are constructed and the {@code static} constructor block is called.
 * This needs to be done to ensure the methods are registered at the correct time, which is usually before {@code ModInitializer$initialize()} is called.
 * Accessing the class before this time will likely result in failure to register correctly or exceptions being thrown.
 * <p>
 * The {@code priority} property can be used if dependencies exist. Higher priorities are registered first.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerManagementInitialized {
    int priority() default -1;
}
