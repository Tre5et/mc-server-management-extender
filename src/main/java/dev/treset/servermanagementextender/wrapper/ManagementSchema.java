package dev.treset.servermanagementextender.wrapper;

import com.mojang.serialization.Codec;
import dev.treset.servermanagementextender.wrapper.enumeration.EnumTransformer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.api.PlayerDto;
import net.minecraft.server.jsonrpc.api.ReferenceUtil;
import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.api.SchemaComponent;
import net.minecraft.server.jsonrpc.methods.Message;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A wrapper containing codec and schema required for RPC methods.
 * @param <T> The type of object the schema represents.
 */
public class ManagementSchema<T> {
    private final Schema<T> schema;
    private final String name;

    public ManagementSchema(Schema<T> schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    public ManagementSchema(SchemaComponent<T> schema) {
        this.schema = schema == null ? null : schema.schema();
        this.name = schema == null ? null : schema.name();
    }

    public Schema<T> getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    /**
     * Converts this schema to a list schema of the same type.
     * @return The list schema of the same type.
     */
    public ManagementSchema<List<T>> asList() {
        return new ManagementSchema<>(schema == null ? null : schema.asArray(), name);
    }

    /**
     * Creates a builder for a management schema.
     * @param identifier The identifier of the schema. Must be unique.
     * @return A management schema builder.
     * @param <T> The type of object this schema represents. Must sometimes be manually specified since the compiler can't always infer it correctly.
     */
    public static <T> RecordSchemaBuilder.RecordSchemaBuilder0<T> builder(Identifier identifier) {
        return new RecordSchemaBuilder.RecordSchemaBuilder0<>(identifier);
    }

    /**
     * Creates a builder for a management schema extension.
     * @param namespace The namespace the schema should be in.
     * @param name The name of the schema. Must be unique.
     * @return A management schema builder.
     * @param <T> The type of object this schema represents. Must sometimes be manually specified since the compiler can't always infer it correctly.
     */
    public static <T> RecordSchemaBuilder.RecordSchemaBuilder0<T> builder(String namespace, String name) {
        return builder(Identifier.fromNamespaceAndPath(namespace, name));
    }

    /**
     * Calls a function with a placeholder RPC schema and the accompanying schema builder to allow building schemas with recursive properties.
     * @param identifier The identifier of the schema. Must be unique.
     * @param builderFunction A function getting an RPC schema builder as the first parameter and a SchemaWrapper placeholder as the second parameter.
     *                        The SchemaWrapper parameter can be used to define recursive parameters by using it in the {@code parameter} or {@code optionalParameter} builder methods.
     *                        Must return a SchemaWrapper of the same type.
     * @return The constructed management schema.
     * @param <T> The type of object this schema represents.
     */
    public static <T> ManagementSchema<T> recursive(Identifier identifier, BiFunction<RecordSchemaBuilder.RecordSchemaBuilder0<T>, ManagementSchema<T>, ManagementSchema<T>> builderFunction) {
        Codec<T> codec = Codec.recursive(identifier.toString(), c -> builderFunction.apply(builder(identifier), new ManagementSchema<>(Schema.record(c), identifier.toString())).getSchema().codec());
        ManagementSchema<T> wrapper = builderFunction.apply(builder(identifier), new ManagementSchema<>(Schema.ofRef(ReferenceUtil.createLocalReference(identifier.toString()), codec), identifier.toString()));
        return new ManagementSchema<>(wrapper.getSchema(), identifier.toString());
    }

    /**
     * Calls a function with a placeholder RPC schema and the accompanying schema builder to allow building schemas with recursive properties.
     * @param namespace The namespace the schema should be in.
     * @param name The name of the schema.
     * @param builderFunction A function getting an RPC schema builder as the first parameter and a SchemaWrapper placeholder as the second parameter.
     *                        The SchemaWrapper parameter can be used to define recursive parameters by using it in the {@code parameter} or {@code optionalParameter} builder methods.
     *                        Must return a SchemaWrapper of the same type.
     * @return The constructed management schema.
     * @param <T> The type of object this schema represents.
     */
    public static <T> ManagementSchema<T> recursive(String namespace, String name, BiFunction<RecordSchemaBuilder.RecordSchemaBuilder0<T>, ManagementSchema<T>, ManagementSchema<T>> builderFunction) {
        return recursive(Identifier.fromNamespaceAndPath(namespace, name), builderFunction);
    }

    /**
     * Creates a management schema from an enum.
     * @param name The name of the schema to create.
     * @param values A function supplying all enum values. Typically {@code Enum::values}.
     * @param transformer A {@code EnumTransformer} used to transform the enum value names to JSON strings.
     * @return The constructed management schema.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> ManagementSchema<T> ofEnum(String name, T[] values, EnumTransformer<T> transformer) {
        List<String> list = Stream.of(values).map(transformer::transform).toList();
        Function<String, T> function = StringRepresentable.createNameLookup(values, transformer::transform);
        Codec<T> codec = ExtraCodecs.orCompressed(Codec.stringResolver(transformer::transform, function), ExtraCodecs.idResolverCodec(Enum::ordinal, (ordinal) -> ordinal >= 0 && ordinal < values.length ? values[ordinal] : null, -1));
        return new ManagementSchema<>(
                Schema.ofEnum(list, codec),
                name
        );
    }

    /**
     * Creates a management schema from an enum.
     * @param name The name of the schema to create.
     * @param values A function supplying all enum values. Typically {@code Enum::values}.
     * @return The constructed management schema.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> ManagementSchema<T> ofEnum(String name, T[] values) {
        return ofEnum(name, values, EnumTransformer.basic());
    }

    /**
     * Creates a management schema from an enum.
     * @param enumClass The enum to create the management schema for.
     * @param transformer A {@code EnumTransformer} used to transform the enum value names to JSON strings.
     * @return The constructed management schema.
     * @param <T> The type of the enum.
     * @throws IllegalArgumentException If there is an error getting the values from the enum.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> ManagementSchema<T> ofEnum(Class<T> enumClass, EnumTransformer<T> transformer) throws IllegalArgumentException {
        try {
            Method valuesMethod = getValuesMethod(enumClass);
            valuesMethod.setAccessible(true);
            return ofEnum(enumClass.getSimpleName(), (T[])valuesMethod.invoke(null), transformer);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Class " + enumClass.getSimpleName() + " is not an enum: no values() method", e);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to run values method on enum class: " + enumClass.getSimpleName(), e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Failed to convert enum values to correct type: " + enumClass.getSimpleName(), e);
        }
    }

    /**
     * Creates a management schema from an enum.
     * @param enumClass The enum to create the management schema for.
     * @return The constructed management schema.
     * @param <T> The type of the enum.
     * @throws IllegalArgumentException If there is an error getting the values from the enum.
     */
    public static <T extends Enum<T>> ManagementSchema<T> ofEnum(Class<T> enumClass) throws IllegalArgumentException {
        return ofEnum(enumClass, EnumTransformer.basic());
    }

    private static <T extends Enum<T>> Method getValuesMethod(Class<T> enumClass) throws NoSuchMethodException, IllegalArgumentException {
        Method valuesMethod = enumClass.getMethod("values");
        if(!Modifier.isStatic(valuesMethod.getModifiers())) {
            throw new IllegalArgumentException("Class \"" + enumClass.getSimpleName() + "\" is not an enum: values() method is not static");
        }
        if(!valuesMethod.getReturnType().isArray()) {
            throw new IllegalArgumentException("Class \"" + enumClass.getSimpleName() + "\" is not an enum: values() method does not return an array");
        }
        if(valuesMethod.getReturnType() != enumClass.arrayType()) {
            throw new IllegalArgumentException("Class \"" + enumClass.getSimpleName() + "\" is not an enum: values() method returns array of wrong type; expected \"" + enumClass.getSimpleName() + "\", got \"" + valuesMethod.getReturnType().arrayType().getSimpleName() + "\"");
        }
        return valuesMethod;
    }

    public static ManagementSchema<Boolean> BOOLEAN = new ManagementSchema<>(Schema.BOOL_SCHEMA, "boolean");
    public static ManagementSchema<Integer> INTEGER = new ManagementSchema<>(Schema.INT_SCHEMA, "integer");
    public static ManagementSchema<Double> DOUBLE = new ManagementSchema<>(Schema.ofType("double", Codec.DOUBLE), "double");
    public static ManagementSchema<String> STRING = new ManagementSchema<>(Schema.STRING_SCHEMA, "string");
    public static ManagementSchema<PlayerDto> PLAYER = new ManagementSchema<>(Schema.PLAYER_SCHEMA);
    public static ManagementSchema<Message> MESSAGE = new ManagementSchema<>(Schema.MESSAGE_SCHEMA);
}
