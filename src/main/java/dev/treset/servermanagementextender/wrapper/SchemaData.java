package dev.treset.servermanagementextender.wrapper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.dedicated.management.schema.RpcSchema;

import java.util.Optional;
import java.util.function.Function;

public record SchemaData<T,A>(
        String name,
        RecordCodecBuilder<T,A> codecBuilder,
        RpcSchema schema
) {
    public RpcSchema applyToSchema(RpcSchema schema) {
        return schema.withProperty(name, this.schema);
    }

    public static <T,A> SchemaData<T,A> of(String name, Codec<A> codec, RpcSchema schema, Function<T,A> getter) {
        return new SchemaData<>(name, codec == null ? null : codec.fieldOf(name).forGetter(getter), schema);
    }

    public static <T,A> SchemaData<T,A> of(String name, ManagementSchema<A> wrapper, Function<T,A> getter) {
        return of(name, wrapper.getCodec(), wrapper.getSchema(), getter);
    }

    public static <T,A> SchemaData<T,Optional<A>> ofOptional(String name, Codec<A> codec, RpcSchema schema, Function<T, Optional<A>> getter) {
        return new SchemaData<>(name, codec == null ? null : codec.optionalFieldOf(name).forGetter(getter), schema);
    }

    public static <T,A> SchemaData<T,Optional<A>> ofOptional(String name, ManagementSchema<A> wrapper, Function<T,Optional<A>> getter) {
        return ofOptional(name, wrapper.getCodec(), wrapper.getSchema(), getter);
    }
}
