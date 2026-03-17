package dev.treset.servermanagementextender.mixin;

import net.minecraft.server.jsonrpc.api.Schema;
import net.minecraft.server.jsonrpc.api.SchemaComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Schema.class)
public interface SchemaMixin {
    @Invoker("registerSchema")
    static <T> SchemaComponent<T> msme$registerEntry(String reference, Schema<T> schema) { throw new AssertionError(); }
}
