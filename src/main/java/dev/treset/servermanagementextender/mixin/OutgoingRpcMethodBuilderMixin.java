package dev.treset.servermanagementextender.mixin;

import dev.treset.servermanagementextender.accessors.OutgoingRpcMethodBuilderAccessor;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.dedicated.management.OutgoingRpcMethod;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(OutgoingRpcMethod.Builder.class)
public abstract class OutgoingRpcMethodBuilderMixin<T extends OutgoingRpcMethod<?, ?>> implements OutgoingRpcMethodBuilderAccessor<T> {
    @Shadow
    public abstract RegistryEntry.Reference<T> buildAndRegister(Identifier id);

    @Override
    public RegistryEntry.Reference<T> register(Identifier id) {
        return buildAndRegister(id);
    }
}
