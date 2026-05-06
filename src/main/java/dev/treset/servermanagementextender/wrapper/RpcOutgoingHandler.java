package dev.treset.servermanagementextender.wrapper;

import dev.treset.servermanagementextender.ServerManagementExtender;
import net.minecraft.core.Holder;
import net.minecraft.server.jsonrpc.Connection;
import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
import net.minecraft.server.jsonrpc.api.SchemaComponent;

import java.util.function.BiConsumer;

/**
 * Allows sending an RPC notification.
 * @param <T> The type of object the notification sends.
 */
public abstract class RpcOutgoingHandler<T, R> {
    protected final Holder.Reference<? extends OutgoingRpcMethod<T, R>> method;

    public RpcOutgoingHandler(Holder.Reference<? extends OutgoingRpcMethod<T, R>> method) {
        this.method = method;
    }

    /**
     * Creates an RPC notification builder.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcOutgoingBuilder.RpcResponselessOutgoingBuilder<T> builder(ManagementSchema<T> schema) {
        return RpcOutgoingBuilder.of(schema);
    }

    /**
     * Creates an RPC notification builder.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcOutgoingBuilder.RpcResponselessOutgoingBuilder<T> builder(SchemaComponent<T> schema) {
        return RpcOutgoingBuilder.of(schema);
    }

    public static class RpcResponselessOutgoingHandler<T> extends RpcOutgoingHandler<T, Void> {
        public RpcResponselessOutgoingHandler(Holder.Reference<? extends OutgoingRpcMethod<T, Void>> method) {
            super(method);
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

    public static class RpcRespondingOutgoingHandler<T,R> extends RpcOutgoingHandler<T,R> {
        public RpcRespondingOutgoingHandler(Holder.Reference<? extends OutgoingRpcMethod<T, R>> method) {
            super(method);
        }


        /**
         * Sends an RPC request containing the data of the object in the configured format to all clients.
         * @param data THe data object to send.
         * @param responseConsumer A method that is called when a response is received from a client.
         * @param errorConsumer A method that is called when an error is received from a client.
         */
        public void sendAll(T data, BiConsumer<R, Connection> responseConsumer, BiConsumer<Exception, Connection> errorConsumer) {
            ServerManagementExtender.requestAll(
                    method,
                    data,
                    responseConsumer,
                    errorConsumer
            );
        }

        /**
         * Sends an RPC request containing the data of the object in the configured format to all clients and waits for the first response.
         * @param data The data object to send.
         * @throws Exception If there is an error retrieving the response.
         */
        public R send(T data) throws Exception {
            return ServerManagementExtender.requestFirstBlocking(
                    method,
                    data
            );
        }
    }
}
