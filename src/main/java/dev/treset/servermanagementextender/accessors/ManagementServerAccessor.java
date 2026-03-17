package dev.treset.servermanagementextender.accessors;

import net.minecraft.server.jsonrpc.Connection;

import java.util.function.Consumer;

public interface ManagementServerAccessor {
    void forEachConnection(Consumer<Connection> task);
}
