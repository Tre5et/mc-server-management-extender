package dev.treset.servermanagementextender.accessors;

import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.IncomingRpcMethod;

public interface IncomingRpcMethodBuilderAccessor<T extends IncomingRpcMethod<?,?>> {
    T register(Identifier id);
}
