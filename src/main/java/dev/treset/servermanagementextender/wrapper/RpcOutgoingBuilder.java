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
public abstract class RpcOutgoingBuilder<T> {
    String name;
    final Schema<T> schema;
    Identifier identifier;
    String description;

    private RpcOutgoingBuilder(String name, Schema<T> schema) {
        this.name = name;
        this.schema = schema;
    }

    /**
     * Creates an RPC notification builder.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcResponselessOutgoingBuilder<T> of(SchemaComponent<T> schema) {
        return new RpcResponselessOutgoingBuilder<>(schema.name(), schema.schema());
    }

    /**
     * Creates an RPC notification builder.
     * @param schema The schema of the notification content.
     * @return The RPC notification builder.
     * @param <T> The type of object the notification sends.
     */
    public static <T> RpcResponselessOutgoingBuilder<T> of(ManagementSchema<T> schema) {
        return new RpcResponselessOutgoingBuilder<>(schema.getName(), schema.getSchema());
    }

    public static class RpcResponselessOutgoingBuilder<T> extends RpcOutgoingBuilder<T> {
        private RpcResponselessOutgoingBuilder(String name, Schema<T> schema) {
            super(name, schema);
        }

        /**
         * Sets the identifier of the notification method. Required to be called before building.
         * @param namespace The namespace of the notification method.
         * @param path The path of the notification method.
         * @return The changed notification method builder.
         */
        public RpcResponselessOutgoingBuilder<T> identifier(String namespace, String path) {
            this.identifier = Identifier.fromNamespaceAndPath(namespace, path);
            return this;
        }

        /**
         * Sets the description of the notification method.
         * @param description The description of the notification method.
         * @return The changed notification method builder.
         */
        public RpcResponselessOutgoingBuilder<T> description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the name of the property in the notification. Default is the schema name.
         * @param name The name of the property.
         * @return The changed notification method builder.
         */
        public RpcResponselessOutgoingBuilder<T> propertyName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Makes the outgoing method builder a request builder that expects a response.
         * @param schema The schema of the response data.
         * @return The request method builder.
         * @param <R> The response type of the request.
         */
        public <R> RpcRespondingOutgoingBuilder<T,R> withResponse(SchemaComponent<R> schema) {
            return new RpcRespondingOutgoingBuilder<>(this, schema.name(), schema.schema());
        }

        /**
         * Makes the outgoing method builder a request builder that expects a response.
         * @param schema The schema of the response data.
         * @return The request method builder.
         * @param <R> The response type of the request.
         */
        public <R> RpcRespondingOutgoingBuilder<T,R> withResponse(ManagementSchema<R> schema) {
            return new RpcRespondingOutgoingBuilder<>(this, schema.getName(), schema.getSchema());
        }

        /**
         * Builds and registers the notification method. An identifier is required before building.
         * @return An RPC notification handler containing a method to send the notification.
         */
        public RpcOutgoingHandler.RpcResponselessOutgoingHandler<T> build() {
            if(identifier == null) {
                throw new IllegalStateException("Identifier is not set");
            }

            OutgoingRpcMethod.OutgoingRpcMethodBuilder<T, Void> builder = OutgoingRpcMethod.notificationWithParams();
            if(description != null) {
                builder.description(description);
            }
            builder.param(name, schema);

            Holder.Reference<? extends OutgoingRpcMethod<T, Void>> method = ((OutgoingRpcMethodBuilderAccessor<? extends OutgoingRpcMethod<T, Void>>)builder)
                    .buildAndRegister(identifier);

            return new RpcOutgoingHandler.RpcResponselessOutgoingHandler<>(method);
        }
    }

    public static class RpcRespondingOutgoingBuilder<T,R> extends RpcOutgoingBuilder<T> {
        String responseName;
        Schema<R> responseSchema;

        private RpcRespondingOutgoingBuilder(
                RpcResponselessOutgoingBuilder<T> builder,
                String responseName,
                Schema<R> responseSchema
        ) {
            super(builder.name, builder.schema);
            this.identifier = builder.identifier;
            this.description = builder.description;
            this.responseName = responseName;
            this.responseSchema = responseSchema;
        }

        /**
         * Sets the identifier of the request method. Required to be called before building.
         * @param namespace The namespace of the request method.
         * @param path The path of the request method.
         * @return The changed request method builder.
         */
        public RpcRespondingOutgoingBuilder<T,R> identifier(String namespace, String path) {
            this.identifier = Identifier.fromNamespaceAndPath(namespace, path);
            return this;
        }

        /**
         * Sets the description of the request method.
         * @param description The description of the request method.
         * @return The changed request method builder.
         */
        public RpcRespondingOutgoingBuilder<T,R> description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the name of the property in the request. Default is the schema name.
         * @param name The name of the property.
         * @return The changed request method builder.
         */
        public RpcRespondingOutgoingBuilder<T,R> propertyName(String name) {
            this.name = name;
            return this;
        }

        public RpcRespondingOutgoingBuilder<T,R> responseName(String responseName) {
            this.responseName = responseName;
            return this;
        }

        /**
         * Builds and registers the request method. An identifier is required before building.
         * @return An RPC request handler containing a method to send the request.
         */
        public RpcOutgoingHandler.RpcRespondingOutgoingHandler<T,R> build() {
            if(identifier == null) {
                throw new IllegalStateException("Identifier is not set");
            }

            OutgoingRpcMethod.OutgoingRpcMethodBuilder<T, R> builder = OutgoingRpcMethod.requestWithParams();
            if(description != null) {
                builder.description(description);
            }
            builder.param(name, schema);
            builder.response(responseName, responseSchema);

            Holder.Reference<? extends OutgoingRpcMethod<T, R>> method = ((OutgoingRpcMethodBuilderAccessor<? extends OutgoingRpcMethod<T, R>>)builder)
                    .buildAndRegister(identifier);

            return new RpcOutgoingHandler.RpcRespondingOutgoingHandler<>(method);
        }
    }
}
