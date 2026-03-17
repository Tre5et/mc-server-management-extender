package dev.treset.servermanagementextender.accessors;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.OutgoingRpcMethod;

public interface OutgoingRpcMethodBuilderAccessor<T extends OutgoingRpcMethod<?, ?>> {
    Holder.Reference<T> buildAndRegister(Identifier id);
}
