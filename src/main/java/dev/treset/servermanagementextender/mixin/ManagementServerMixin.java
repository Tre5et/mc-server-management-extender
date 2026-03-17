package dev.treset.servermanagementextender.mixin;

import net.minecraft.server.jsonrpc.Connection;
import net.minecraft.server.jsonrpc.ManagementServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(ManagementServer.class)
public interface ManagementServerMixin {
    @Invoker("forEachConnection")
    void msme$forEachConnection(Consumer<Connection> task);
}
