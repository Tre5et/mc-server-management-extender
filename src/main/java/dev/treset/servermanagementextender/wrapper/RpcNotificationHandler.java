package dev.treset.servermanagementextender.wrapper;

import dev.treset.servermanagementextender.ServerManagementExtender;
import net.minecraft.core.Holder;
import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
import net.minecraft.server.jsonrpc.api.SchemaComponent;

/**
 * Allows sending an RPC notification.
 * @param <T> The type of object the notification sends.
 */
public class RpcNotificationHandler<T> {
    private final Holder.Reference<? extends OutgoingRpcMethod<T, ?>> method;

    public RpcNotificationHandler(Holder.Reference<? extends OutgoingRpcMethod<T, ?>> method) {
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
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcNotificationBuilder<T> builder(SchemaComponent<T> schema) {
        return RpcNotificationBuilder.of(schema);
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
