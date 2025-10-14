package dev.treset.servermanagementextender.accessors;

import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;

public interface RpcSchemaAccessor {
    RpcSchemaEntry register(String name, RpcSchema schema);
}
