package dev.treset.servermanagementextender.mixin;

import net.minecraft.server.dedicated.management.ManagementServer;
import net.minecraft.server.dedicated.management.network.ManagementConnectionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(ManagementServer.class)
public interface ManagementServerMixin {
    @Invoker("forEachConnection")
    void msme$forEachConnection(Consumer<ManagementConnectionHandler> task);
}
