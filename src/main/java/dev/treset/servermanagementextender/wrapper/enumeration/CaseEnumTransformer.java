package dev.treset.servermanagementextender.wrapper.enumeration;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

class CaseEnumTransformer<T extends Enum<T>> extends BasicNameEnumTransformer<T> {
    protected CaseEnumTransformer(Function<Stream<String>, String> transformer) {
        super(s -> transformer.apply(determineParts(s)));
    }

    private static Stream<String> determineParts(String valueString) {
        if(valueString.matches(".*[_-].*")) {
            // Contains '-' or '_'; Assume snake case or kebab case.
            return Arrays.stream(valueString.split("[_-]")).filter(s -> !s.isBlank());
        }
        if(valueString.matches("\\p{Lu}*") || valueString.matches("\\p{Ll}*")) {
            // No splitting points; Assume unchanged
            return Stream.of(valueString);
        }
        // Assume camel case or pascal case
        return Arrays.stream(valueString.split("(?=[\\p{Lu}_-])")).filter(s -> !s.isBlank())
                .map(s -> s.replaceAll("[_-]", ""));
    }
}
