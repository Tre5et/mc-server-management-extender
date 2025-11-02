package dev.treset.servermanagementextender.wrapper.enumeration;

import java.util.function.Function;

class BasicNameEnumTransformer<T extends Enum<T>> extends EnumTransformer<T> {
    protected BasicNameEnumTransformer(Function<String, String> transformer) {
        super(transformer, Enum::name);
    }
}
