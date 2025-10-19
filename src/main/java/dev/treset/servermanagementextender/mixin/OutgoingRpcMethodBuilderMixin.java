package dev.treset.servermanagementextender.mixin;

import dev.treset.servermanagementextender.accessors.OutgoingRpcMethodBuilderAccessor;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.dedicated.management.OutgoingRpcMethod;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(OutgoingRpcMethod.Builder.class)
public abstract class OutgoingRpcMethodBuilderMixin<T extends OutgoingRpcMethod<?, ?>> implements OutgoingRpcMethodBuilderAccessor<T> {
    @Invoker("buildAndRegister")
    public abstract RegistryEntry.Reference<T> msme$buildAndRegister(Identifier id);

    @Override
    public RegistryEntry.Reference<T> register(Identifier id) {
        return msme$buildAndRegister(id);
    }
}
