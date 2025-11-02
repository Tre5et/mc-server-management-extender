package dev.treset.servermanagementextender.wrapper.enumeration;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Transforms an enums values to a different serialized format.
 * <br><br>Example:
 * <pre>
 * {@code
 * @ServerManagementInitialized
 * enum MyEnum {
 *     MY_FIRST_VALUE,
 *     MY_SECOND_VALUE;
 *
 *     public static final ManagementSchema<MyEnum> SCHEMA = ManagementSchema.ofEnum(
 *             MyEnum.class,
 *             EnumTransformer.pascalCase()
 *     );
 *     // Expected format in RPC message: MyFirstValue / MySecondValue
 * }
 * }
 * </pre>
 * @param <T> The type of the enum.
 */
public abstract class EnumTransformer<T extends Enum<T>> {
    private EnumTransformer<T> next;
    private final Function<String, String> transformer;
    private final Function<T, String> nameGetter;

    public EnumTransformer(Function<String, String> transformer, Function<T, String> nameGetter) {
        this.transformer = transformer;
        this.nameGetter = nameGetter;
    }

    /**
     * Registers a new enum transformer at the end of the transformation pipeline. This transformer is executed on the result of all previously registered transformers.
     * @param next The new transformer to register.
     * @return The current transformer.
     */
    public final EnumTransformer<T> then(EnumTransformer<T> next) {
        if(this.next == null) {
            this.next = next;
        } else {
            this.next.then(next);
        }
        return this;
    }

    public final String transformName(String name) {
        String res = transformer.apply(name);
        if(next == null) {
            return res;
        }
        return next.transformName(res);
    }

    public final String getName(T value) {
        return nameGetter.apply(value);
    }

    public final String transform(T value) {
        return transformName(getName(value));
    }

    /**
     * Constructs an enum transformer returning the field names with no transformation.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> basic() {
        return new BasicNameEnumTransformer<>(s -> s);
    }

    /**
     * Constructs an enum transformer converting the field names to {@code lowercase}.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> lowercase() {
        return new BasicNameEnumTransformer<>(String::toLowerCase);
    }

    /**
     * Constructs an enum transformer converting the field names to {@code UPPERCASE}.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> uppercase() {
        return new BasicNameEnumTransformer<>(String::toUpperCase);
    }

    /**
     * Constructs an enum transformer converting the field names to {@code CAPITALIZED_SNAKE_CASE}.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> snakeCaseUpper() {
        return new CaseEnumTransformer<>(s -> s.map(String::toUpperCase).collect(Collectors.joining("_")));
    }

    /**
     * Constructs an enum transformer converting the field names to {@code uncapitalized_snake_case}.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> snakeCaseLower() {
        return new CaseEnumTransformer<>(s -> s.map(String::toLowerCase).collect(Collectors.joining("_")));
    }

    /**
     * Constructs an enum transformer converting the field names to {@code CAPITALIZED-KEBAB-CASE.}
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> kebabCaseUpper() {
        return new CaseEnumTransformer<>(s -> s.map(String::toUpperCase).collect(Collectors.joining("-")));
    }

    /**
     * Constructs an enum transformer converting the field names to {@code uncapitalized-kebab-case}.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> kebabCaseLower() {
        return new CaseEnumTransformer<>(s -> s.map(String::toLowerCase).collect(Collectors.joining("-")));
    }

    /**
     * Constructs an enum transformer converting the field names to {@code PascalCase}.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> pascalCase() {
        return new CaseEnumTransformer<>(s -> s.map(String::toLowerCase).map(s1 -> s1.replaceFirst("^.", String.valueOf(s1.charAt(0)).toUpperCase())).collect(Collectors.joining()));
    }

    /**
     * Constructs an enum transformer converting the field names to {@code camelCase}.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> camelCase() {
        return new CaseEnumTransformer<>(s -> {
            String pascal = s.map(String::toLowerCase).map(s1 -> s1.replaceFirst("^.", String.valueOf(s1.charAt(0)).toUpperCase())).collect(Collectors.joining());
            return pascal.replaceFirst("^.", String.valueOf(pascal.charAt(0)).toLowerCase());
        });
    }

    /**
     * Constructs an enum transformer converting the field names to a custom name using a custom function.
     * <br><br>Not effective when used in {@code EnumTransformer.then()}.
     * <br><br>Example:
     * <pre>
     * {@code
     * @ServerManagementInitialized
     * enum MyEnum {
     *     VALUE1("serialized_name_1"),
     *     VALUE2("serialized_name_2"),
     *     VALUE3("serialized_name_3");
     *
     *     private final String serializedName;
     *
     *     MyEnum(String serializedName) {
     *         this.serializedName = serializedName;
     *     }
     *
     *     public String serialized() {
     *         return serializedName;
     *     }
     *
     *     public static final ManagementSchema<MyEnum> SCHEMA = ManagementSchema.ofEnum(
     *             MyEnum.class,
     *             EnumTransformer.customName(MyEnum::serialized)
     *     );
     * }
     * }
     * </pre>
     * @param nameGetter A function getting the custom name of the enum field.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> customName(Function<T, String> nameGetter) {
        return new BasicTransformEnumTransformer<>(nameGetter);
    }

    /**
     * Constructs an enum transformer converting the field names to a custom format using a custom function.
     * @param transformer A function transforming the field name to a new format.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> custom(Function<String, String> transformer) {
        return new BasicNameEnumTransformer<>(transformer);
    }

    /**
     * Constructs an enum transformer converting the field names to a custom format using a function on the name that is pre-split.
     * @param transformer A function transforming a stream of the parts of the field name to a new format.
     * @return The constructed enum transformer.
     * @param <T> The type of the enum.
     */
    public static <T extends Enum<T>> EnumTransformer<T> customCase(Function<Stream<String>, String> transformer) {
        return new CaseEnumTransformer<>(transformer);
    }
}
