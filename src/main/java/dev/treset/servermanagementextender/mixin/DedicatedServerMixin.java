package dev.treset.servermanagementextender.mixin;

import dev.treset.servermanagementextender.ServerManagementExtender;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.jsonrpc.ManagementServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin {
    @Accessor("jsonRpcServer")
    public abstract ManagementServer getJsonRpcServer();

    @Inject(method = "initServer()Z", at = @At("RETURN"))
    private void initServer(CallbackInfoReturnable<Boolean> info) {
        ServerManagementExtender.init(getJsonRpcServer());
    }
}
