package dev.treset.servermanagementextender;

import dev.treset.servermanagementextender.mixin.ManagementServerMixin;
import net.fabricmc.api.ModInitializer;

import net.minecraft.core.Holder;
import net.minecraft.server.jsonrpc.ManagementServer;
import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerManagementExtender implements ModInitializer {
	public static final String MOD_ID = "server-management-extender";
	public static final Logger LOGGER = LoggerFactory.getLogger("Server Management Extender");

    private static ManagementServer managementServer;

    public static void init(ManagementServer server) {
        managementServer = server;
        if(managementServer == null) {
            ServerManagementExtender.LOGGER.error("Failed to get Management Server!");
        } else {
            ServerManagementExtender.LOGGER.info("Initialized!");
        }
    }

    public static boolean isInitialized() {
        return managementServer != null;
    }

    public static <T> void notifyAll(
            Holder.Reference<? extends OutgoingRpcMethod<T, ?>> method,
            T payload
            ) {
        if (!isInitialized() || method == null) return;
        ((ManagementServerMixin)managementServer)
                .msme$forEachConnection(connection ->
                        connection.sendNotification(method, payload)
                );
    }

	@Override
	public void onInitialize() {}
}