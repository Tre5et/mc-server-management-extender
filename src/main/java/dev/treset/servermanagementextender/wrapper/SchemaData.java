package dev.treset.servermanagementextender.wrapper;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.dedicated.management.schema.RpcSchema;

import java.util.Optional;
import java.util.function.Function;

public record SchemaData<T,A>(
        String name,
        RecordCodecBuilder<T,A> codecBuilder,
        RpcSchema<?> schema
) {
    public RpcSchema<T> applyToSchema(RpcSchema<T> schema) {
        return schema.withProperty(name, this.schema);
    }

    public static <T,A> SchemaData<T,A> of(String name, RpcSchema<A> schema, Function<T,A> getter) {
        return new SchemaData<>(name, schema.codec() == null ? null : schema.codec().fieldOf(name).forGetter(getter), schema);
    }

    public static <T,A> SchemaData<T,A> of(String name, ManagementSchema<A> wrapper, Function<T,A> getter) {
        return of(name, wrapper.getSchema(), getter);
    }

    public static <T,A> SchemaData<T,Optional<A>> ofOptional(String name, RpcSchema<A> schema, Function<T, Optional<A>> getter) {
        return new SchemaData<>(name, schema.codec() == null ? null : schema.codec().optionalFieldOf(name).forGetter(getter), schema);
    }

    public static <T,A> SchemaData<T,Optional<A>> ofOptional(String name, ManagementSchema<A> wrapper, Function<T,Optional<A>> getter) {
        return ofOptional(name, wrapper.getSchema(), getter);
    }
}
