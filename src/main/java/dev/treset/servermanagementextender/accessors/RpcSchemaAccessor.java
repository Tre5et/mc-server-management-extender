package dev.treset.servermanagementextender.accessors;

import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.api.SchemaComponent;

public interface RpcSchemaAccessor<T> {
    SchemaComponent<T> register(String name, Schema<T> schema);
}
