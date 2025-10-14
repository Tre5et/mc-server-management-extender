package dev.treset.servermanagementextender.mixin;

import dev.treset.servermanagementextender.accessors.IncomingRpcMethodBuilderAccessor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.dedicated.management.IncomingRpcMethod;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IncomingRpcMethod.Builder.class)
public abstract class IncomingRpcMethodBuilderMixin<T extends IncomingRpcMethod> implements IncomingRpcMethodBuilderAccessor<T> {

    @Shadow
    @Override
    public abstract T buildAndRegister(Registry<IncomingRpcMethod> registry, Identifier id);

    @Override
    public T register(Identifier id) {
        return buildAndRegister(Registries.INCOMING_RPC_METHOD, id);
    }
}
