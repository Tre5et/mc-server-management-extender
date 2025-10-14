package dev.treset.servermanagementextender.accessors;

import net.minecraft.registry.Registry;
import net.minecraft.server.dedicated.management.IncomingRpcMethod;
import net.minecraft.util.Identifier;

public interface IncomingRpcMethodBuilderAccessor<T extends IncomingRpcMethod> {
    T buildAndRegister(Registry<IncomingRpcMethod> registry, Identifier id);
    T register(Identifier id);
}
