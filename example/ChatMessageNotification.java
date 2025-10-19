package dev.treset.servermanagementextender;

import dev.treset.servermanagementextender.wrapper.RpcNotificationHandler;
import dev.treset.servermanagementextender.wrapper.ServerManagementInitialized;

@ServerManagementInitialized
public class ChatMessageNotification {
    public static final RpcNotificationHandler<RpcChatMessage> HANDLER = RpcNotificationHandler
            .builder(RpcChatMessage.SCHEMA)
            .description("Chat message was sent")
            .identifier("your_mod_id", "notification/chat_message")
            .build();
}
