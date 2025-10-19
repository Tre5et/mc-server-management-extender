package dev.treset.servermanagementextender.accessors;

import net.minecraft.server.dedicated.management.IncomingRpcMethod;
import net.minecraft.util.Identifier;

public interface IncomingRpcMethodBuilderAccessor<T extends IncomingRpcMethod> {
    T register(Identifier id);
}
