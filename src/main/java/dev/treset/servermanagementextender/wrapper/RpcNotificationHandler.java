package dev.treset.servermanagementextender.wrapper;

import com.mojang.serialization.Codec;
import dev.treset.servermanagementextender.ServerManagementExtender;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.dedicated.management.OutgoingRpcMethod;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;

/**
 * Allows sending an RPC notification.
 * @param <T> The type of object the notification sends.
 */
public class RpcNotificationHandler<T> {
    private final RegistryEntry.Reference<? extends OutgoingRpcMethod<T, ?>> method;

    public RpcNotificationHandler(RegistryEntry.Reference<? extends OutgoingRpcMethod<T, ?>> method) {
        this.method = method;
    }

    /**
     * Creates an RPC notification builder.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcNotificationBuilder<T> builder(ManagementSchema<T> schema) {
        return RpcNotificationBuilder.of(schema);
    }

    /**
     * Creates an RPC notification builder.
     * @param codec The codec of the notification content.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcNotificationBuilder<T> builder(Codec<T> codec, RpcSchemaEntry schema) {
        return RpcNotificationBuilder.of(codec, schema);
    }

    /**
     * Sends an RPC notification containing the data of the object in the configured format to all clients.
     * @param data The data object to send.
     */
    public void send(T data) {
        ServerManagementExtender.notifyAll(
                method,
                data
        );
    }
}
