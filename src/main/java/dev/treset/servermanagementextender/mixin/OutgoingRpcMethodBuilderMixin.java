package dev.treset.servermanagementextender.mixin;

import dev.treset.servermanagementextender.accessors.OutgoingRpcMethodBuilderAccessor;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(OutgoingRpcMethod.OutgoingRpcMethodBuilder.class)
public abstract class OutgoingRpcMethodBuilderMixin<T extends OutgoingRpcMethod<?, ?>> implements OutgoingRpcMethodBuilderAccessor<T> {
    @Invoker("register")
    public abstract Holder.Reference<T> msme$register(Identifier id);

    @Override
    public Holder.Reference<T> buildAndRegister(Identifier id) {
        return msme$register(id);
    }
}
