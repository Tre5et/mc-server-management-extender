package dev.treset.servermanagementextender.wrapper;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.jsonrpc.api.Schema;

import java.util.Optional;
import java.util.function.Function;

public record SchemaData<T,A>(
        String name,
        RecordCodecBuilder<T,A> codecBuilder,
        Schema<?> schema
) {
    public Schema<T> applyToSchema(Schema<T> schema) {
        return schema.withField(name, this.schema);
    }

    public static <T,A> SchemaData<T,A> of(String name, Schema<A> schema, Function<T,A> getter) {
        return new SchemaData<>(name, schema.codec().fieldOf(name).forGetter(getter), schema);
    }

    public static <T,A> SchemaData<T,A> of(String name, ManagementSchema<A> wrapper, Function<T,A> getter) {
        return of(name, wrapper.getSchema(), getter);
    }

    public static <T,A> SchemaData<T,Optional<A>> ofOptional(String name, Schema<A> schema, Function<T, Optional<A>> getter) {
        return new SchemaData<>(name, schema.codec().optionalFieldOf(name).forGetter(getter), schema);
    }

    public static <T,A> SchemaData<T,Optional<A>> ofOptional(String name, ManagementSchema<A> wrapper, Function<T,Optional<A>> getter) {
        return ofOptional(name, wrapper.getSchema(), getter);
    }
}
