package dev.treset.servermanagementextender.mixin;

import dev.treset.servermanagementextender.ServerManagementExtender;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.dedicated.management.ManagementServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin {
    @Accessor("managementServer")
    public abstract ManagementServer getManagementServer();

    @Inject(method = "setupServer()Z", at = @At("RETURN"))
    private void setupServer(CallbackInfoReturnable<Boolean> info) {
        ServerManagementExtender.init(getManagementServer());
        ServerManagementExtender.notifyAll(null, null);
    }
}
