package dev.treset.servermanagementextender.wrapper;

import com.mojang.datafixers.util.Function3;
import com.mojang.serialization.Codec;
import dev.treset.servermanagementextender.accessors.IncomingRpcMethodBuilderAccessor;
import net.minecraft.server.dedicated.management.IncomingRpcMethod;
import net.minecraft.server.dedicated.management.RpcRequestParameter;
import net.minecraft.server.dedicated.management.RpcResponseResult;
import net.minecraft.server.dedicated.management.dispatch.ManagementHandlerDispatcher;
import net.minecraft.server.dedicated.management.network.ManagementConnectionId;
import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

/**
 * Allows configuration, building and registering of an RPC request method.
 * @param <R> The type of object contained in the response to the RPC request.
 */
public abstract class RpcMethodBuilder<R> {
    protected String name;
    protected final Codec<R> codec;
    protected final RpcSchema schema;
    protected Identifier identifier;
    protected String description;

    private RpcMethodBuilder(String name, Codec<R> codec, RpcSchema schema) {
        this.name = name;
        this.codec = codec;
        this.schema = schema;
    }

    /**
     * Creates an RPC request method builder.
     * @param codec The codec of the RPC response.
     * @param schema The schema of the RPC response.
     * @return The RPC request method builder.
     * @param <R> The type of object contained in the RPC response.
     */
    public static <R> RpcParameterlessMethodBuilder<R> of(Codec<R> codec, RpcSchemaEntry schema) {
        return new RpcParameterlessMethodBuilder<>(schema.name(), codec, schema.schema());
    }

    /**
     * Creates an RPC request method builder.
     * @param wrapper The schema wrapper of the RPC response.
     * @return The RPC request method builder.
     * @param <R> The type of object contained in the RPC response.
     */
    public static <R> RpcParameterlessMethodBuilder<R> of(ManagementSchema<R> wrapper) {
        return new RpcParameterlessMethodBuilder<>(wrapper.getName(), wrapper.getCodec(), wrapper.getSchema());
    }

    public static class RpcParameterlessMethodBuilder<R> extends RpcMethodBuilder<R> {
        private RpcParameterlessMethodBuilder(String name, Codec<R> codec, RpcSchema schema) {
            super(name, codec, schema);
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
            this.identifier = Identifier.of(namespace, path);
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
         * @param wrapper The schema wrapper of the parameter content.
         * @return A new RPC request method builder containing the parameter.
         * @param <T> The type of object in the RPC request parameter.
         */
        public <T> RpcParametrizedMethodBuilder<T,R> parameter(ManagementSchema<T> wrapper) {
            return new RpcParametrizedMethodBuilder<>(
                    name,
                    codec,
                    schema,
                    identifier,
                    description,
                    wrapper.getName(),
                    wrapper.getCodec(),
                    wrapper.getSchema()
            );
        }

        /**
         * Builds and registers the RPC request method. An identifier is required before building.
         * @param handler A function that is called when a request is received, taking a {@code ManagementHandlerDispatcher} and returning the data to be sent in the response.
         * @return The created method. Can generally be ignored.
         */
        @SuppressWarnings("unchecked")
        public IncomingRpcMethod.Parameterless<R> build(Function<ManagementHandlerDispatcher, R> handler) {
            if(identifier == null) {
                throw new IllegalStateException("Identifier is not set");
            }

            IncomingRpcMethod.Builder<IncomingRpcMethod.Parameterless<R>> builder = IncomingRpcMethod.createParameterlessBuilder(
                    handler,
                    codec
            ).result(
                    new RpcResponseResult(name, schema)
            );

            if(description != null) {
                builder = builder.description(description);
            }

            return ((IncomingRpcMethodBuilderAccessor<IncomingRpcMethod.Parameterless<R>>)builder)
                    .register(identifier);
        }
    }

    public static class RpcParametrizedMethodBuilder<T,R> extends RpcMethodBuilder<R> {
        private String parameterName;
        private final Codec<T> parameterCodec;
        private final RpcSchema parameterSchema;

        private RpcParametrizedMethodBuilder(String name, Codec<R> codec, RpcSchema schema, Identifier identifier, String description, String parameterName, Codec<T> parameterCodec, RpcSchema parameterSchema) {
            super(name, codec, schema);
            this.identifier = identifier;
            this.description = description;
            this.parameterCodec = parameterCodec;
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
            this.identifier = Identifier.of(namespace, path);
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
        public IncomingRpcMethod.Parameterized<T,R> build(Function3<ManagementHandlerDispatcher, T, ManagementConnectionId, R> handler) {
            if(identifier == null) {
                throw new IllegalStateException("Identifier is not set");
            }

            IncomingRpcMethod.Builder<IncomingRpcMethod.Parameterized<T,R>> builder = IncomingRpcMethod.createParameterizedBuilder(
                    handler::apply,
                    parameterCodec,
                    codec
            ).parameter(
                    new RpcRequestParameter(
                            parameterName,
                            parameterSchema
                    )
            ).result(
                    new RpcResponseResult(name, schema)
            );

            if(description != null) {
                builder = builder.description(description);
            }

            return ((IncomingRpcMethodBuilderAccessor<IncomingRpcMethod.Parameterized<T,R>>)builder)
                    .register(identifier);
        }
    }
}
