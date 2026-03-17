package dev.treset.servermanagementextender.wrapper;

import com.mojang.datafixers.util.Function3;
import dev.treset.servermanagementextender.accessors.IncomingRpcMethodBuilderAccessor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.IncomingRpcMethod;
import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.api.SchemaComponent;
import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
import net.minecraft.server.jsonrpc.methods.ClientInfo;

import java.util.function.Function;

/**
 * Allows configuration, building and registering of an RPC request method.
 * @param <R> The type of object contained in the response to the RPC request.
 */
public abstract class RpcMethodBuilder<R> {
    protected String name;
    protected final Schema<R> schema;
    protected Identifier identifier;
    protected String description;

    private RpcMethodBuilder(String name, Schema<R> schema) {
        this.name = name;
        this.schema = schema;
    }

    /**
     * Creates an RPC request method builder.
     * @param schema The schema of the RPC response.
     * @return The RPC request method builder.
     * @param <R> The type of object contained in the RPC response.
     */
    public static <R> RpcParameterlessMethodBuilder<R> of(SchemaComponent<R> schema) {
        return new RpcParameterlessMethodBuilder<>(schema.name(), schema.schema());
    }

    /**
     * Creates an RPC request method builder.
     * @param schema The schema of the RPC response.
     * @return The RPC request method builder.
     * @param <R> The type of object contained in the RPC response.
     */
    public static <R> RpcParameterlessMethodBuilder<R> of(ManagementSchema<R> schema) {
        return new RpcParameterlessMethodBuilder<>(schema.getName(), schema.getSchema());
    }

    public static class RpcParameterlessMethodBuilder<R> extends RpcMethodBuilder<R> {
        private RpcParameterlessMethodBuilder(String name, Schema<R> schema) {
            super(name, schema);
        }

        /**
         * Sets the name of the property in the RPC response. Default is the schema name.
         * @param name The name of the property.
         * @return The changed RPC request method builder.
         */
        public RpcParameterlessMethodBuilder<R> responsePropertyName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the identifier of the RPC request method. Required to be called before building.
         * @param namespace The namespace of the RPC request method.
         * @param path The path of the RPC request method.
         * @return The changed RPC request method builder.
         */
        public RpcParameterlessMethodBuilder<R> identifier(String namespace, String path) {
            this.identifier = Identifier.fromNamespaceAndPath(namespace, path);
            return this;
        }

        /**
         * Sets the description of the RPC request method.
         * @param description The description of the RPC request method.
         * @return The changed RPC request method builder.
         */
        public RpcParameterlessMethodBuilder<R> description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Adds a parameter requirement to the RPC request method.
         * @param schema The schema of the parameter content.
         * @return A new RPC request method builder containing the parameter.
         * @param <T> The type of object in the RPC request parameter.
         */
        public <T> RpcParametrizedMethodBuilder<T,R> parameter(ManagementSchema<T> schema) {
            return new RpcParametrizedMethodBuilder<>(
                    name,
                    this.schema,
                    identifier,
                    description,
                    schema.getName(),
                    schema.getSchema()
            );
        }

        /**
         * Builds and registers the RPC request method. An identifier is required before building.
         * @param handler A function that is called when a request is received, taking a {@code ManagementHandlerDispatcher} and returning the data to be sent in the response.
         * @return The created method. Can generally be ignored.
         */
        @SuppressWarnings("unchecked")
        public IncomingRpcMethod.ParameterlessMethod<Void, R> build(Function<MinecraftApi, R> handler) {
            if(identifier == null) {
                throw new IllegalStateException("Identifier is not set");
            }

            IncomingRpcMethod.IncomingRpcMethodBuilder<Void, R> builder = IncomingRpcMethod.method(
                    handler
            ).response(
                    name, schema
            );

            if(description != null) {
                builder = builder.description(description);
            }

            return ((IncomingRpcMethodBuilderAccessor<IncomingRpcMethod.ParameterlessMethod<Void, R>>)builder)
                    .register(identifier);
        }
    }

    public static class RpcParametrizedMethodBuilder<T,R> extends RpcMethodBuilder<R> {
        private String parameterName;
        private final Schema<T> parameterSchema;

        private RpcParametrizedMethodBuilder(String name, Schema<R> schema, Identifier identifier, String description, String parameterName, Schema<T> parameterSchema) {
            super(name, schema);
            this.identifier = identifier;
            this.description = description;
            this.parameterSchema = parameterSchema;
            this.parameterName = parameterName;
        }

        /**
         * Sets the name of the property in the RPC response. Default is the schema name.
         * @param name The name of the property.
         * @return The changed RPC request method builder.
         */
        public RpcParametrizedMethodBuilder<T,R> responsePropertyName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the name of the parameter in the RPC request. Default is the schema name.
         * @param name The name of the property.
         * @return The changed RPC request method builder.
         */
        public RpcParametrizedMethodBuilder<T,R> parameterName(String name) {
            this.parameterName = name;
            return this;
        }

        /**
         * Sets the identifier of the RPC request method. Required to be called before building.
         * @param namespace The namespace of the RPC request method.
         * @param path The path of the RPC request method.
         * @return The changed RPC request method builder.
         */
        public RpcParametrizedMethodBuilder<T,R> identifier(String namespace, String path) {
            this.identifier = Identifier.fromNamespaceAndPath(namespace, path);
            return this;
        }

        /**
         * Sets the description of the RPC request method.
         * @param description The description of the RPC request method.
         * @return The changed RPC request method builder.
         */
        public RpcParametrizedMethodBuilder<T,R> description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Builds and registers the RPC request method. An identifier is required before building.
         * @param handler A function that is called when a request is received, taking a {@code ManagementHandlerDispatcher}, the data sent in the parameter and a {@code ManagementConnectionId} and returning the data to be sent in the response.
         * @return The created method. Can generally be ignored.
         */
        @SuppressWarnings("unchecked")
        public IncomingRpcMethod.Method<T,R> build(Function3<MinecraftApi, T, ClientInfo, R> handler) {
            if(identifier == null) {
                throw new IllegalStateException("Identifier is not set");
            }

            IncomingRpcMethod.IncomingRpcMethodBuilder<T,R> builder = IncomingRpcMethod.method(
                    handler::apply
            ).param(
                    name,
                    parameterSchema
            ).response(
                    name, schema
            );

            if(description != null) {
                builder = builder.description(description);
            }

            return ((IncomingRpcMethodBuilderAccessor<IncomingRpcMethod.Method<T,R>>)builder)
                    .register(identifier);
        }
    }
}
