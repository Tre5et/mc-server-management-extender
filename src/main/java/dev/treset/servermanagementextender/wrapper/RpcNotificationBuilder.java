package dev.treset.servermanagementextender.wrapper;

import dev.treset.servermanagementextender.accessors.OutgoingRpcMethodBuilderAccessor;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.api.SchemaComponent;

/**
 * Allows configuration, building and registering of an RPC notification method.
 * @param <T> The type of object sent by the notification.
 */
public class RpcNotificationBuilder<T> {
    private String name;
    private final Schema<T> schema;
    private Identifier identifier;
    private String description;

    private RpcNotificationBuilder(String name, Schema<T> schema) {
        this.name = name;
        this.schema = schema;
    }

    /**
     * Creates an RPC notification builder.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcNotificationBuilder<T> of(SchemaComponent<T> schema) {
        return new RpcNotificationBuilder<>(schema.name(), schema.schema());
    }

    /**
     * Creates an RPC notification builder.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcNotificationBuilder<T> of(ManagementSchema<T> schema) {
        return new RpcNotificationBuilder<>(schema.getName(), schema.getSchema());
    }

    /**
     * Sets the identifier of the notification method. Required to be called before building.
     * @param namespace The namespace of the notification method.
     * @param path The path of the notification method.
     * @return The changed notification method builder.
     */
    public RpcNotificationBuilder<T> identifier(String namespace, String path) {
        this.identifier = Identifier.fromNamespaceAndPath(namespace, path);
        return this;
    }

    /**
     * Sets the description of the notification method.
     * @param description The description of the notification method.
     * @return The changed notification method builder.
     */
    public RpcNotificationBuilder<T> description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the name of the property in the notification. Default is the schema name.
     * @param name The name of the property.
     * @return The changed notification method builder.
     */
    public RpcNotificationBuilder<T> propertyName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Builds and registers the notification method. An identifier is required before building.
     * @return An RPC notification handler containing a method to send the notification.
     */
    public RpcNotificationHandler<T> build() {
        if(identifier == null) {
            throw new IllegalStateException("Identifier is not set");
        }

        OutgoingRpcMethod.OutgoingRpcMethodBuilder<T, Void> builder = OutgoingRpcMethod.notificationWithParams();
        if(description != null) {
            builder.description(description);
        }
        builder.param(name, schema);

        Holder.Reference<? extends OutgoingRpcMethod<T, ?>> method = ((OutgoingRpcMethodBuilderAccessor<? extends OutgoingRpcMethod<T, ?>>)builder)
                .buildAndRegister(identifier);

        return new RpcNotificationHandler<>(method);
    }
}
