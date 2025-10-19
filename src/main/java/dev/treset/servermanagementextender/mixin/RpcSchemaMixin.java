package dev.treset.servermanagementextender.mixin;


import net.minecraft.server.dedicated.management.schema.RpcSchema;
import net.minecraft.server.dedicated.management.schema.RpcSchemaEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RpcSchema.class)
public interface RpcSchemaMixin {
    @Invoker("registerEntry")
    static RpcSchemaEntry msme$registerEntry(String reference, RpcSchema schema) { throw new AssertionError(); }
}
