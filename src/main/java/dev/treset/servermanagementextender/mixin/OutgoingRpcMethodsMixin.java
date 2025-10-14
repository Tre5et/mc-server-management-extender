package dev.treset.servermanagementextender.mixin;

import dev.treset.servermanagementextender.wrapper.RpcRegistration;
import net.minecraft.server.dedicated.management.OutgoingRpcMethods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OutgoingRpcMethods.class)
public class OutgoingRpcMethodsMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerOutgoingRpc(CallbackInfo ci) {
        RpcRegistration.applyRegister();
    }
}
