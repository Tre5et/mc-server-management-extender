package dev.treset.servermanagementextender;

import dev.treset.servermanagementextender.accessors.ManagementServerAccessor;
import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.dedicated.management.ManagementServer;
import net.minecraft.server.dedicated.management.OutgoingRpcMethod;
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
        return managementServer == null;
    }

    public static <T> void notifyAll(
            RegistryEntry.Reference<? extends OutgoingRpcMethod<T, ?>> method,
            T payload
    ) {
        if (isInitialized()) return;
        ((ManagementServerAccessor)managementServer)
                .forEachConnection(connection ->
                        connection.sendNotification(method, payload)
                );
    }

	@Override
	public void onInitialize() {}
}