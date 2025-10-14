package dev.treset.servermanagementextender.wrapper;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.treset.servermanagementextender.mixin.RpcSchemaMixin;
import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class RecordSchemaBuilder<T> {
    protected Identifier identifier;

    protected abstract List<SchemaData<T,?>> propertiesList();

    protected ManagementSchema<T> buildInternal(Function<RecordCodecBuilder.Instance<T>, App<RecordCodecBuilder.Mu<T>, T>> createInstance) {
        List<SchemaData<T,?>> props = propertiesList();

        Codec<T> codec = null;
        RpcSchemaEntry schemaEntry = null;

        if(props.stream().allMatch(p -> p.codecBuilder() != null)) {
            codec = RecordCodecBuilder.create(createInstance);
        }

        if(props.stream().allMatch(p -> p.schema() != null)) {
            RpcSchema schema = RpcSchema.ofObject();
            for (SchemaData<T, ?> p : propertiesList()) {
                schema = p.applyToSchema(schema);
            }
            schemaEntry = RpcSchemaMixin.registerEntry(identifier.toString(), schema);
        }

        return new ManagementSchema<>(codec, schemaEntry);
    }

    public static class RecordSchemaBuilder0<T> extends RecordSchemaBuilder<T> {
        public RecordSchemaBuilder0(Identifier identifier) {
            this.identifier = identifier;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T1> The type of object that is stored in the property.
         */
        public <T1> RecordSchemaBuilder1<T,T1> property(SchemaData<T,T1> data) {
            return new RecordSchemaBuilder1<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T1> The type of object that is stored in the property.
         */
        public <T1> RecordSchemaBuilder1<T,T1> property(String name, Codec<T1> codec, RpcSchema schema, Function<T,T1> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T1> The type of object that is stored in the property.
         */
        public <T1> RecordSchemaBuilder1<T,T1> property(String name, ManagementSchema<T1> schema, Function<T,T1> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T1> The type of object that is stored in the property.
         */
        public <T1> RecordSchemaBuilder1<T,Optional<T1>> optionalProperty(String name, Codec<T1> codec, RpcSchema schema, Function<T,Optional<T1>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T1> The type of object that is stored in the property.
         */
        public <T1> RecordSchemaBuilder1<T,Optional<T1>> optionalProperty(String name, ManagementSchema<T1> schema, Function<T,Optional<T1>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            throw new IllegalStateException("No properties added, cannot list them");
        }
    }

    public static class RecordSchemaBuilder1<T,T1> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;

        public RecordSchemaBuilder1(RecordSchemaBuilder0<T> builder, SchemaData<T,T1> p1) {
            this.identifier = builder.identifier;
            this.p1 = p1;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T2> The type of object that is stored in the property.
         */
        public <T2> RecordSchemaBuilder2<T,T1,T2> property(SchemaData<T,T2> data) {
            return new RecordSchemaBuilder2<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T2> The type of object that is stored in the property.
         */
        public <T2> RecordSchemaBuilder2<T,T1,T2> property(String name, Codec<T2> codec, RpcSchema schema, Function<T,T2> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T2> The type of object that is stored in the property.
         */
        public <T2> RecordSchemaBuilder2<T,T1,T2> property(String name, ManagementSchema<T2> schema, Function<T,T2> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <2> The type of object that is stored in the property.
         */
        public <T2> RecordSchemaBuilder2<T,T1,Optional<T2>> optionalProperty(String name, Codec<T2> codec, RpcSchema schema, Function<T,Optional<T2>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T2> The type of object that is stored in the property.
         */
        public <T2> RecordSchemaBuilder2<T,T1,Optional<T2>> optionalProperty(String name, ManagementSchema<T2> schema, Function<T,Optional<T2>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function<T1,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1);
        }
    }
    public static class RecordSchemaBuilder2<T,T1,T2> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;

        public RecordSchemaBuilder2(RecordSchemaBuilder1<T,T1> builder, SchemaData<T,T2> p2) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = p2;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T3> The type of object that is stored in the property.
         */
        public <T3> RecordSchemaBuilder3<T,T1,T2,T3> property(SchemaData<T,T3> data) {
            return new RecordSchemaBuilder3<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T3> The type of object that is stored in the property.
         */
        public <T3> RecordSchemaBuilder3<T,T1,T2,T3> property(String name, Codec<T3> codec, RpcSchema schema, Function<T,T3> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T3> The type of object that is stored in the property.
         */
        public <T3> RecordSchemaBuilder3<T,T1,T2,T3> property(String name, ManagementSchema<T3> schema, Function<T,T3> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <3> The type of object that is stored in the property.
         */
        public <T3> RecordSchemaBuilder3<T,T1,T2,Optional<T3>> optionalProperty(String name, Codec<T3> codec, RpcSchema schema, Function<T,Optional<T3>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T3> The type of object that is stored in the property.
         */
        public <T3> RecordSchemaBuilder3<T,T1,T2,Optional<T3>> optionalProperty(String name, ManagementSchema<T3> schema, Function<T,Optional<T3>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(BiFunction<T1,T2,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2);
        }
    }
    public static class RecordSchemaBuilder3<T,T1,T2,T3> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;

        public RecordSchemaBuilder3(RecordSchemaBuilder2<T,T1,T2> builder, SchemaData<T,T3> p3) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = p3;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T4> The type of object that is stored in the property.
         */
        public <T4> RecordSchemaBuilder4<T,T1,T2,T3,T4> property(SchemaData<T,T4> data) {
            return new RecordSchemaBuilder4<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T4> The type of object that is stored in the property.
         */
        public <T4> RecordSchemaBuilder4<T,T1,T2,T3,T4> property(String name, Codec<T4> codec, RpcSchema schema, Function<T,T4> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T4> The type of object that is stored in the property.
         */
        public <T4> RecordSchemaBuilder4<T,T1,T2,T3,T4> property(String name, ManagementSchema<T4> schema, Function<T,T4> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <4> The type of object that is stored in the property.
         */
        public <T4> RecordSchemaBuilder4<T,T1,T2,T3,Optional<T4>> optionalProperty(String name, Codec<T4> codec, RpcSchema schema, Function<T,Optional<T4>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T4> The type of object that is stored in the property.
         */
        public <T4> RecordSchemaBuilder4<T,T1,T2,T3,Optional<T4>> optionalProperty(String name, ManagementSchema<T4> schema, Function<T,Optional<T4>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function3<T1,T2,T3,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3);
        }
    }
    public static class RecordSchemaBuilder4<T,T1,T2,T3,T4> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;

        public RecordSchemaBuilder4(RecordSchemaBuilder3<T,T1,T2,T3> builder, SchemaData<T,T4> p4) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = p4;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T5> The type of object that is stored in the property.
         */
        public <T5> RecordSchemaBuilder5<T,T1,T2,T3,T4,T5> property(SchemaData<T,T5> data) {
            return new RecordSchemaBuilder5<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T5> The type of object that is stored in the property.
         */
        public <T5> RecordSchemaBuilder5<T,T1,T2,T3,T4,T5> property(String name, Codec<T5> codec, RpcSchema schema, Function<T,T5> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T5> The type of object that is stored in the property.
         */
        public <T5> RecordSchemaBuilder5<T,T1,T2,T3,T4,T5> property(String name, ManagementSchema<T5> schema, Function<T,T5> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <5> The type of object that is stored in the property.
         */
        public <T5> RecordSchemaBuilder5<T,T1,T2,T3,T4,Optional<T5>> optionalProperty(String name, Codec<T5> codec, RpcSchema schema, Function<T,Optional<T5>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T5> The type of object that is stored in the property.
         */
        public <T5> RecordSchemaBuilder5<T,T1,T2,T3,T4,Optional<T5>> optionalProperty(String name, ManagementSchema<T5> schema, Function<T,Optional<T5>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function4<T1,T2,T3,T4,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4);
        }
    }
    public static class RecordSchemaBuilder5<T,T1,T2,T3,T4,T5> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;

        public RecordSchemaBuilder5(RecordSchemaBuilder4<T,T1,T2,T3,T4> builder, SchemaData<T,T5> p5) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = p5;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T6> The type of object that is stored in the property.
         */
        public <T6> RecordSchemaBuilder6<T,T1,T2,T3,T4,T5,T6> property(SchemaData<T,T6> data) {
            return new RecordSchemaBuilder6<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T6> The type of object that is stored in the property.
         */
        public <T6> RecordSchemaBuilder6<T,T1,T2,T3,T4,T5,T6> property(String name, Codec<T6> codec, RpcSchema schema, Function<T,T6> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T6> The type of object that is stored in the property.
         */
        public <T6> RecordSchemaBuilder6<T,T1,T2,T3,T4,T5,T6> property(String name, ManagementSchema<T6> schema, Function<T,T6> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <6> The type of object that is stored in the property.
         */
        public <T6> RecordSchemaBuilder6<T,T1,T2,T3,T4,T5,Optional<T6>> optionalProperty(String name, Codec<T6> codec, RpcSchema schema, Function<T,Optional<T6>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T6> The type of object that is stored in the property.
         */
        public <T6> RecordSchemaBuilder6<T,T1,T2,T3,T4,T5,Optional<T6>> optionalProperty(String name, ManagementSchema<T6> schema, Function<T,Optional<T6>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function5<T1,T2,T3,T4,T5,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5);
        }
    }
    public static class RecordSchemaBuilder6<T,T1,T2,T3,T4,T5,T6> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;

        public RecordSchemaBuilder6(RecordSchemaBuilder5<T,T1,T2,T3,T4,T5> builder, SchemaData<T,T6> p6) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = p6;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T7> The type of object that is stored in the property.
         */
        public <T7> RecordSchemaBuilder7<T,T1,T2,T3,T4,T5,T6,T7> property(SchemaData<T,T7> data) {
            return new RecordSchemaBuilder7<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T7> The type of object that is stored in the property.
         */
        public <T7> RecordSchemaBuilder7<T,T1,T2,T3,T4,T5,T6,T7> property(String name, Codec<T7> codec, RpcSchema schema, Function<T,T7> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T7> The type of object that is stored in the property.
         */
        public <T7> RecordSchemaBuilder7<T,T1,T2,T3,T4,T5,T6,T7> property(String name, ManagementSchema<T7> schema, Function<T,T7> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <7> The type of object that is stored in the property.
         */
        public <T7> RecordSchemaBuilder7<T,T1,T2,T3,T4,T5,T6,Optional<T7>> optionalProperty(String name, Codec<T7> codec, RpcSchema schema, Function<T,Optional<T7>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T7> The type of object that is stored in the property.
         */
        public <T7> RecordSchemaBuilder7<T,T1,T2,T3,T4,T5,T6,Optional<T7>> optionalProperty(String name, ManagementSchema<T7> schema, Function<T,Optional<T7>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function6<T1,T2,T3,T4,T5,T6,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6);
        }
    }
    public static class RecordSchemaBuilder7<T,T1,T2,T3,T4,T5,T6,T7> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;

        public RecordSchemaBuilder7(RecordSchemaBuilder6<T,T1,T2,T3,T4,T5,T6> builder, SchemaData<T,T7> p7) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = p7;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T8> The type of object that is stored in the property.
         */
        public <T8> RecordSchemaBuilder8<T,T1,T2,T3,T4,T5,T6,T7,T8> property(SchemaData<T,T8> data) {
            return new RecordSchemaBuilder8<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T8> The type of object that is stored in the property.
         */
        public <T8> RecordSchemaBuilder8<T,T1,T2,T3,T4,T5,T6,T7,T8> property(String name, Codec<T8> codec, RpcSchema schema, Function<T,T8> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T8> The type of object that is stored in the property.
         */
        public <T8> RecordSchemaBuilder8<T,T1,T2,T3,T4,T5,T6,T7,T8> property(String name, ManagementSchema<T8> schema, Function<T,T8> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <8> The type of object that is stored in the property.
         */
        public <T8> RecordSchemaBuilder8<T,T1,T2,T3,T4,T5,T6,T7,Optional<T8>> optionalProperty(String name, Codec<T8> codec, RpcSchema schema, Function<T,Optional<T8>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T8> The type of object that is stored in the property.
         */
        public <T8> RecordSchemaBuilder8<T,T1,T2,T3,T4,T5,T6,T7,Optional<T8>> optionalProperty(String name, ManagementSchema<T8> schema, Function<T,Optional<T8>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function7<T1,T2,T3,T4,T5,T6,T7,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7);
        }
    }
    public static class RecordSchemaBuilder8<T,T1,T2,T3,T4,T5,T6,T7,T8> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;
        private final SchemaData<T,T8> p8;

        public RecordSchemaBuilder8(RecordSchemaBuilder7<T,T1,T2,T3,T4,T5,T6,T7> builder, SchemaData<T,T8> p8) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = builder.p7;
            this.p8 = p8;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T9> The type of object that is stored in the property.
         */
        public <T9> RecordSchemaBuilder9<T,T1,T2,T3,T4,T5,T6,T7,T8,T9> property(SchemaData<T,T9> data) {
            return new RecordSchemaBuilder9<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T9> The type of object that is stored in the property.
         */
        public <T9> RecordSchemaBuilder9<T,T1,T2,T3,T4,T5,T6,T7,T8,T9> property(String name, Codec<T9> codec, RpcSchema schema, Function<T,T9> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T9> The type of object that is stored in the property.
         */
        public <T9> RecordSchemaBuilder9<T,T1,T2,T3,T4,T5,T6,T7,T8,T9> property(String name, ManagementSchema<T9> schema, Function<T,T9> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <9> The type of object that is stored in the property.
         */
        public <T9> RecordSchemaBuilder9<T,T1,T2,T3,T4,T5,T6,T7,T8,Optional<T9>> optionalProperty(String name, Codec<T9> codec, RpcSchema schema, Function<T,Optional<T9>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T9> The type of object that is stored in the property.
         */
        public <T9> RecordSchemaBuilder9<T,T1,T2,T3,T4,T5,T6,T7,T8,Optional<T9>> optionalProperty(String name, ManagementSchema<T9> schema, Function<T,Optional<T9>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function8<T1,T2,T3,T4,T5,T6,T7,T8,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder(),
                            p8.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7,p8);
        }
    }
    public static class RecordSchemaBuilder9<T,T1,T2,T3,T4,T5,T6,T7,T8,T9> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;
        private final SchemaData<T,T8> p8;
        private final SchemaData<T,T9> p9;

        public RecordSchemaBuilder9(RecordSchemaBuilder8<T,T1,T2,T3,T4,T5,T6,T7,T8> builder, SchemaData<T,T9> p9) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = builder.p7;
            this.p8 = builder.p8;
            this.p9 = p9;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T10> The type of object that is stored in the property.
         */
        public <T10> RecordSchemaBuilder10<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> property(SchemaData<T,T10> data) {
            return new RecordSchemaBuilder10<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T10> The type of object that is stored in the property.
         */
        public <T10> RecordSchemaBuilder10<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> property(String name, Codec<T10> codec, RpcSchema schema, Function<T,T10> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T10> The type of object that is stored in the property.
         */
        public <T10> RecordSchemaBuilder10<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> property(String name, ManagementSchema<T10> schema, Function<T,T10> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <10> The type of object that is stored in the property.
         */
        public <T10> RecordSchemaBuilder10<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,Optional<T10>> optionalProperty(String name, Codec<T10> codec, RpcSchema schema, Function<T,Optional<T10>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T10> The type of object that is stored in the property.
         */
        public <T10> RecordSchemaBuilder10<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,Optional<T10>> optionalProperty(String name, ManagementSchema<T10> schema, Function<T,Optional<T10>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function9<T1,T2,T3,T4,T5,T6,T7,T8,T9,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder(),
                            p8.codecBuilder(),
                            p9.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9);
        }
    }
    public static class RecordSchemaBuilder10<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;
        private final SchemaData<T,T8> p8;
        private final SchemaData<T,T9> p9;
        private final SchemaData<T,T10> p10;

        public RecordSchemaBuilder10(RecordSchemaBuilder9<T,T1,T2,T3,T4,T5,T6,T7,T8,T9> builder, SchemaData<T,T10> p10) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = builder.p7;
            this.p8 = builder.p8;
            this.p9 = builder.p9;
            this.p10 = p10;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T11> The type of object that is stored in the property.
         */
        public <T11> RecordSchemaBuilder11<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> property(SchemaData<T,T11> data) {
            return new RecordSchemaBuilder11<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T11> The type of object that is stored in the property.
         */
        public <T11> RecordSchemaBuilder11<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> property(String name, Codec<T11> codec, RpcSchema schema, Function<T,T11> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T11> The type of object that is stored in the property.
         */
        public <T11> RecordSchemaBuilder11<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> property(String name, ManagementSchema<T11> schema, Function<T,T11> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <11> The type of object that is stored in the property.
         */
        public <T11> RecordSchemaBuilder11<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,Optional<T11>> optionalProperty(String name, Codec<T11> codec, RpcSchema schema, Function<T,Optional<T11>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T11> The type of object that is stored in the property.
         */
        public <T11> RecordSchemaBuilder11<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,Optional<T11>> optionalProperty(String name, ManagementSchema<T11> schema, Function<T,Optional<T11>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function10<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder(),
                            p8.codecBuilder(),
                            p9.codecBuilder(),
                            p10.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10);
        }
    }
    public static class RecordSchemaBuilder11<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;
        private final SchemaData<T,T8> p8;
        private final SchemaData<T,T9> p9;
        private final SchemaData<T,T10> p10;
        private final SchemaData<T,T11> p11;

        public RecordSchemaBuilder11(RecordSchemaBuilder10<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> builder, SchemaData<T,T11> p11) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = builder.p7;
            this.p8 = builder.p8;
            this.p9 = builder.p9;
            this.p10 = builder.p10;
            this.p11 = p11;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T12> The type of object that is stored in the property.
         */
        public <T12> RecordSchemaBuilder12<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> property(SchemaData<T,T12> data) {
            return new RecordSchemaBuilder12<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T12> The type of object that is stored in the property.
         */
        public <T12> RecordSchemaBuilder12<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> property(String name, Codec<T12> codec, RpcSchema schema, Function<T,T12> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T12> The type of object that is stored in the property.
         */
        public <T12> RecordSchemaBuilder12<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> property(String name, ManagementSchema<T12> schema, Function<T,T12> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <12> The type of object that is stored in the property.
         */
        public <T12> RecordSchemaBuilder12<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,Optional<T12>> optionalProperty(String name, Codec<T12> codec, RpcSchema schema, Function<T,Optional<T12>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T12> The type of object that is stored in the property.
         */
        public <T12> RecordSchemaBuilder12<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,Optional<T12>> optionalProperty(String name, ManagementSchema<T12> schema, Function<T,Optional<T12>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function11<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder(),
                            p8.codecBuilder(),
                            p9.codecBuilder(),
                            p10.codecBuilder(),
                            p11.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11);
        }
    }
    public static class RecordSchemaBuilder12<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;
        private final SchemaData<T,T8> p8;
        private final SchemaData<T,T9> p9;
        private final SchemaData<T,T10> p10;
        private final SchemaData<T,T11> p11;
        private final SchemaData<T,T12> p12;

        public RecordSchemaBuilder12(RecordSchemaBuilder11<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> builder, SchemaData<T,T12> p12) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = builder.p7;
            this.p8 = builder.p8;
            this.p9 = builder.p9;
            this.p10 = builder.p10;
            this.p11 = builder.p11;
            this.p12 = p12;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T13> The type of object that is stored in the property.
         */
        public <T13> RecordSchemaBuilder13<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> property(SchemaData<T,T13> data) {
            return new RecordSchemaBuilder13<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T13> The type of object that is stored in the property.
         */
        public <T13> RecordSchemaBuilder13<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> property(String name, Codec<T13> codec, RpcSchema schema, Function<T,T13> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T13> The type of object that is stored in the property.
         */
        public <T13> RecordSchemaBuilder13<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> property(String name, ManagementSchema<T13> schema, Function<T,T13> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <13> The type of object that is stored in the property.
         */
        public <T13> RecordSchemaBuilder13<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,Optional<T13>> optionalProperty(String name, Codec<T13> codec, RpcSchema schema, Function<T,Optional<T13>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T13> The type of object that is stored in the property.
         */
        public <T13> RecordSchemaBuilder13<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,Optional<T13>> optionalProperty(String name, ManagementSchema<T13> schema, Function<T,Optional<T13>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function12<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder(),
                            p8.codecBuilder(),
                            p9.codecBuilder(),
                            p10.codecBuilder(),
                            p11.codecBuilder(),
                            p12.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12);
        }
    }
    public static class RecordSchemaBuilder13<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;
        private final SchemaData<T,T8> p8;
        private final SchemaData<T,T9> p9;
        private final SchemaData<T,T10> p10;
        private final SchemaData<T,T11> p11;
        private final SchemaData<T,T12> p12;
        private final SchemaData<T,T13> p13;

        public RecordSchemaBuilder13(RecordSchemaBuilder12<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> builder, SchemaData<T,T13> p13) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = builder.p7;
            this.p8 = builder.p8;
            this.p9 = builder.p9;
            this.p10 = builder.p10;
            this.p11 = builder.p11;
            this.p12 = builder.p12;
            this.p13 = p13;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T14> The type of object that is stored in the property.
         */
        public <T14> RecordSchemaBuilder14<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> property(SchemaData<T,T14> data) {
            return new RecordSchemaBuilder14<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T14> The type of object that is stored in the property.
         */
        public <T14> RecordSchemaBuilder14<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> property(String name, Codec<T14> codec, RpcSchema schema, Function<T,T14> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T14> The type of object that is stored in the property.
         */
        public <T14> RecordSchemaBuilder14<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> property(String name, ManagementSchema<T14> schema, Function<T,T14> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <14> The type of object that is stored in the property.
         */
        public <T14> RecordSchemaBuilder14<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,Optional<T14>> optionalProperty(String name, Codec<T14> codec, RpcSchema schema, Function<T,Optional<T14>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T14> The type of object that is stored in the property.
         */
        public <T14> RecordSchemaBuilder14<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,Optional<T14>> optionalProperty(String name, ManagementSchema<T14> schema, Function<T,Optional<T14>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function13<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder(),
                            p8.codecBuilder(),
                            p9.codecBuilder(),
                            p10.codecBuilder(),
                            p11.codecBuilder(),
                            p12.codecBuilder(),
                            p13.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13);
        }
    }
    public static class RecordSchemaBuilder14<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;
        private final SchemaData<T,T8> p8;
        private final SchemaData<T,T9> p9;
        private final SchemaData<T,T10> p10;
        private final SchemaData<T,T11> p11;
        private final SchemaData<T,T12> p12;
        private final SchemaData<T,T13> p13;
        private final SchemaData<T,T14> p14;

        public RecordSchemaBuilder14(RecordSchemaBuilder13<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> builder, SchemaData<T,T14> p14) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = builder.p7;
            this.p8 = builder.p8;
            this.p9 = builder.p9;
            this.p10 = builder.p10;
            this.p11 = builder.p11;
            this.p12 = builder.p12;
            this.p13 = builder.p13;
            this.p14 = p14;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T15> The type of object that is stored in the property.
         */
        public <T15> RecordSchemaBuilder15<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> property(SchemaData<T,T15> data) {
            return new RecordSchemaBuilder15<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T15> The type of object that is stored in the property.
         */
        public <T15> RecordSchemaBuilder15<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> property(String name, Codec<T15> codec, RpcSchema schema, Function<T,T15> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T15> The type of object that is stored in the property.
         */
        public <T15> RecordSchemaBuilder15<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> property(String name, ManagementSchema<T15> schema, Function<T,T15> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <15> The type of object that is stored in the property.
         */
        public <T15> RecordSchemaBuilder15<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,Optional<T15>> optionalProperty(String name, Codec<T15> codec, RpcSchema schema, Function<T,Optional<T15>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T15> The type of object that is stored in the property.
         */
        public <T15> RecordSchemaBuilder15<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,Optional<T15>> optionalProperty(String name, ManagementSchema<T15> schema, Function<T,Optional<T15>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function14<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder(),
                            p8.codecBuilder(),
                            p9.codecBuilder(),
                            p10.codecBuilder(),
                            p11.codecBuilder(),
                            p12.codecBuilder(),
                            p13.codecBuilder(),
                            p14.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14);
        }
    }
    public static class RecordSchemaBuilder15<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;
        private final SchemaData<T,T8> p8;
        private final SchemaData<T,T9> p9;
        private final SchemaData<T,T10> p10;
        private final SchemaData<T,T11> p11;
        private final SchemaData<T,T12> p12;
        private final SchemaData<T,T13> p13;
        private final SchemaData<T,T14> p14;
        private final SchemaData<T,T15> p15;

        public RecordSchemaBuilder15(RecordSchemaBuilder14<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> builder, SchemaData<T,T15> p15) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = builder.p7;
            this.p8 = builder.p8;
            this.p9 = builder.p9;
            this.p10 = builder.p10;
            this.p11 = builder.p11;
            this.p12 = builder.p12;
            this.p13 = builder.p13;
            this.p14 = builder.p14;
            this.p15 = p15;
        }

        /**
         * Adds a property to the schema.
         * @param data The schema data containing the property values.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T16> The type of object that is stored in the property.
         */
        public <T16> RecordSchemaBuilder16<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16> property(SchemaData<T,T16> data) {
            return new RecordSchemaBuilder16<>(this, data);
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T16> The type of object that is stored in the property.
         */
        public <T16> RecordSchemaBuilder16<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16> property(String name, Codec<T16> codec, RpcSchema schema, Function<T,T16> getter) {
            return property(SchemaData.of(name, codec, schema, getter));
        }

        /**
         * Adds a property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T16> The type of object that is stored in the property.
         */
        public <T16> RecordSchemaBuilder16<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16> property(String name, ManagementSchema<T16> schema, Function<T,T16> getter) {
            return property(SchemaData.of(name, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param codec The codec of the property.
         * @param schema The schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <16> The type of object that is stored in the property.
         */
        public <T16> RecordSchemaBuilder16<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,Optional<T16>> optionalProperty(String name, Codec<T16> codec, RpcSchema schema, Function<T,Optional<T16>> getter) {
            return property(SchemaData.ofOptional(name, codec, schema, getter));
        }

        /**
         * Adds an optional property to the schema.
         * @param name The name of the property.
         * @param schema The combined management schema of the property.
         * @param getter A getter to get the optional property value from the parent object represented by the parent schema.
         * @return A new RecordSchemaBuilder containing the new property.
         * @param <T16> The type of object that is stored in the property.
         */
        public <T16> RecordSchemaBuilder16<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,Optional<T16>> optionalProperty(String name, ManagementSchema<T16> schema, Function<T,Optional<T16>> getter) {
            return property(SchemaData.ofOptional(name, schema, getter));
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function15<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder(),
                            p8.codecBuilder(),
                            p9.codecBuilder(),
                            p10.codecBuilder(),
                            p11.codecBuilder(),
                            p12.codecBuilder(),
                            p13.codecBuilder(),
                            p14.codecBuilder(),
                            p15.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15);
        }
    }
    public static class RecordSchemaBuilder16<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16> extends RecordSchemaBuilder<T> {
        private final SchemaData<T,T1> p1;
        private final SchemaData<T,T2> p2;
        private final SchemaData<T,T3> p3;
        private final SchemaData<T,T4> p4;
        private final SchemaData<T,T5> p5;
        private final SchemaData<T,T6> p6;
        private final SchemaData<T,T7> p7;
        private final SchemaData<T,T8> p8;
        private final SchemaData<T,T9> p9;
        private final SchemaData<T,T10> p10;
        private final SchemaData<T,T11> p11;
        private final SchemaData<T,T12> p12;
        private final SchemaData<T,T13> p13;
        private final SchemaData<T,T14> p14;
        private final SchemaData<T,T15> p15;
        private final SchemaData<T,T16> p16;

        public RecordSchemaBuilder16(RecordSchemaBuilder15<T,T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> builder, SchemaData<T,T16> p16) {
            this.identifier = builder.identifier;
            this.p1 = builder.p1;
            this.p2 = builder.p2;
            this.p3 = builder.p3;
            this.p4 = builder.p4;
            this.p5 = builder.p5;
            this.p6 = builder.p6;
            this.p7 = builder.p7;
            this.p8 = builder.p8;
            this.p9 = builder.p9;
            this.p10 = builder.p10;
            this.p11 = builder.p11;
            this.p12 = builder.p12;
            this.p13 = builder.p13;
            this.p14 = builder.p14;
            this.p15 = builder.p15;
            this.p16 = p16;
        }

        /**
         * Builds and registers the management schema.
         * @param applicator A function creating the reference type from all property types.
         * @return The created management schema.
         */
        public ManagementSchema<T> build(Function16<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T> applicator) {
            return buildInternal(
                    i -> i.group(
                            p1.codecBuilder(),
                            p2.codecBuilder(),
                            p3.codecBuilder(),
                            p4.codecBuilder(),
                            p5.codecBuilder(),
                            p6.codecBuilder(),
                            p7.codecBuilder(),
                            p8.codecBuilder(),
                            p9.codecBuilder(),
                            p10.codecBuilder(),
                            p11.codecBuilder(),
                            p12.codecBuilder(),
                            p13.codecBuilder(),
                            p14.codecBuilder(),
                            p15.codecBuilder(),
                            p16.codecBuilder()
                    ).apply(i, applicator)
            );
        }

        @Override
        protected List<SchemaData<T,?>> propertiesList() {
            return List.of(p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16);
        }
    }

}