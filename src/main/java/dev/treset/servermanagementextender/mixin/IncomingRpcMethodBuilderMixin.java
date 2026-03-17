package dev.treset.servermanagementextender.mixin;

import dev.treset.servermanagementextender.accessors.IncomingRpcMethodBuilderAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.jsonrpc.IncomingRpcMethod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(IncomingRpcMethod.IncomingRpcMethodBuilder.class)
public abstract class IncomingRpcMethodBuilderMixin<T extends IncomingRpcMethod<?,?>> implements IncomingRpcMethodBuilderAccessor<T> {

    @Invoker("register")
    public abstract T msme$register(Registry<IncomingRpcMethod<?,?>> registry, Identifier id);

    @Override
    public T register(Identifier id) {
        return msme$register(BuiltInRegistries.INCOMING_RPC_METHOD, id);
    }
}
