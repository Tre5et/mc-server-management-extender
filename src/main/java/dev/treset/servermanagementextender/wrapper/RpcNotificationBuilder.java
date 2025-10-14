package dev.treset.servermanagementextender.wrapper;

import com.mojang.serialization.Codec;
import dev.treset.servermanagementextender.accessors.OutgoingRpcMethodBuilderAccessor;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.dedicated.management.OutgoingRpcMethod;
import net.minecraft.server.dedicated.management.RpcRequestParameter;
import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;
import net.minecraft.util.Identifier;

/**
 * Allows configuration, building and registering of an RPC notification method.
 * @param <T> The type of object sent by the notification.
 */
public class RpcNotificationBuilder<T> {
    private String name;
    private final Codec<T> codec;
    private final RpcSchema schema;
    private Identifier identifier;
    private String description;

    private RpcNotificationBuilder(String name, Codec<T> codec, RpcSchema schema) {
        this.codec = codec;
        this.name = name;
        this.schema = schema;
    }

    /**
     * Creates an RPC notification builder.
     * @param codec The codec of the notification content.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcNotificationBuilder<T> of(Codec<T> codec, RpcSchemaEntry schema) {
        return new RpcNotificationBuilder<>(schema.name(), codec, schema.schema());
    }

    /**
     * Creates an RPC notification builder.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcNotificationBuilder<T> of(ManagementSchema<T> schema) {
        return new RpcNotificationBuilder<>(schema.getName(), schema.getCodec(), schema.getSchema());
    }

    /**
     * Sets the identifier of the notification method. Required to be called before building.
     * @param namespace The namespace of the notification method.
     * @param path The path of the notification method.
     * @return The changed notification method builder.
     */
    public RpcNotificationBuilder<T> identifier(String namespace, String path) {
        this.identifier = Identifier.of(namespace, path);
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
    @SuppressWarnings("unchecked")
    public RpcNotificationHandler<T> build() {
        if(identifier == null) {
            throw new IllegalStateException("Identifier is not set");
        }

        OutgoingRpcMethod.Builder<OutgoingRpcMethod.Notification<T>> builder = OutgoingRpcMethod.createNotificationBuilder(codec);
        if(description != null) {
            builder.description(description);
        }
        builder.requestParameter(new RpcRequestParameter(name, schema));

        RegistryEntry.Reference<? extends OutgoingRpcMethod<T, ?>> method = ((OutgoingRpcMethodBuilderAccessor<? extends OutgoingRpcMethod<T, ?>>)builder)
                .register(identifier);

        return new RpcNotificationHandler<>(method);
    }
}
