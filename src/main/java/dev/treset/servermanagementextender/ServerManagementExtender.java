package dev.treset.servermanagementextender;

import dev.treset.servermanagementextender.mixin.ManagementServerMixin;
import net.fabricmc.api.ModInitializer;

import net.minecraft.core.Holder;
import net.minecraft.server.jsonrpc.Connection;
import net.minecraft.server.jsonrpc.ManagementServer;
import net.minecraft.server.jsonrpc.OutgoingRpcMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class ServerManagementExtender implements ModInitializer {
	public static final String MOD_ID = "server-management-extender";
	public static final Logger LOGGER = LoggerFactory.getLogger("Server Management Extender");

    private static long requestTimeoutSeconds = 15;

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

    public static void setRequestTimeoutSeconds(long requestTimeoutSeconds) {
        ServerManagementExtender.requestTimeoutSeconds = requestTimeoutSeconds;
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

    public static <T,R> void requestAll(
            Holder.Reference<? extends OutgoingRpcMethod<T, R>> method,
            T payload,
            BiConsumer<R, Connection> responseConsumer,
            BiConsumer<Exception, Connection> errorConsumer
    ) {
        if (!isInitialized() || method == null) {
            errorConsumer.accept(new IllegalStateException("Server manager is not configured correctly."), null);
            return;
        }

        ((ManagementServerMixin)managementServer)
                .msme$forEachConnection(connection -> connection.sendRequest(method, payload)
                        .orTimeout(requestTimeoutSeconds, TimeUnit.SECONDS)
                        .thenAccept(response -> responseConsumer.accept(response, connection))
                        .exceptionally(ex -> {
                            errorConsumer.accept((Exception) ex, connection);
                            return null;
                        })
                );
    }

    public static <T,R> R requestFirstBlocking(
            Holder.Reference<? extends OutgoingRpcMethod<T, R>> method,
            T payload
    ) throws Exception {
        CompletableFuture<R> result = new CompletableFuture<>();

        requestAll(
            method,
            payload,
            (r, _) -> result.complete(r),
            (e, _) -> result.completeExceptionally(e)
        );

        return result.get(requestTimeoutSeconds, TimeUnit.SECONDS);
    }

	@Override
	public void onInitialize() {}
}