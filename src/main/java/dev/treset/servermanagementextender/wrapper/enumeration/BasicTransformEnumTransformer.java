package dev.treset.servermanagementextender.wrapper.enumeration;

import java.util.function.Function;

class BasicTransformEnumTransformer<T extends Enum<T>> extends EnumTransformer<T> {
    protected BasicTransformEnumTransformer(Function<T, String> nameGetter) {
        super(s -> s, nameGetter);
    }
}
