package dev.treset.servermanagementextender.accessors;

import net.minecraft.server.dedicated.management.network.ManagementConnectionHandler;

import java.util.function.Consumer;

public interface ManagementServerAccessor {
    void forEachConnection(Consumer<ManagementConnectionHandler> task);
}
