package dev.treset.servermanagementextender;

import dev.treset.servermanagementextender.wrapper.ManagementSchema;
import dev.treset.servermanagementextender.wrapper.ServerManagementInitialized;
import net.minecraft.server.dedicated.management.RpcPlayer;

@ServerManagementInitialized
public record RpcChatMessage(
        RpcPlayer player,
        String message
) {
    public static final ManagementSchema<RpcChatMessage> SCHEMA = ManagementSchema
            .<RpcChatMessage>builder("your_mod_id", "chat_message")
            .property("player", ManagementSchema.PLAYER, RpcChatMessage::player)
            .property("message", ManagementSchema.STRING, RpcChatMessage::message)
            .build(RpcChatMessage::new);
}
