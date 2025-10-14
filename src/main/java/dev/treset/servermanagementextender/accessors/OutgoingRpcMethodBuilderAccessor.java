package dev.treset.servermanagementextender.accessors;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.dedicated.management.OutgoingRpcMethod;
import net.minecraft.util.Identifier;

public interface OutgoingRpcMethodBuilderAccessor<T extends OutgoingRpcMethod<?, ?>> {
    RegistryEntry.Reference<T> register(Identifier id);
}
