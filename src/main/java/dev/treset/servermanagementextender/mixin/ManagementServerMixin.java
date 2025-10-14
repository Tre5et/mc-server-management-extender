package dev.treset.servermanagementextender.mixin;

import dev.treset.servermanagementextender.accessors.ManagementServerAccessor;
import net.minecraft.server.dedicated.management.ManagementServer;
import net.minecraft.server.dedicated.management.network.ManagementConnectionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(ManagementServer.class)
public abstract class ManagementServerMixin implements ManagementServerAccessor {
    @Shadow
    public abstract void forEachConnection(Consumer<ManagementConnectionHandler> task);
}
