package dev.treset.servermanagementextender;

import dev.treset.servermanagementextender.wrapper.ManagementSchema;
import dev.treset.servermanagementextender.wrapper.RpcMethodBuilder;
import dev.treset.servermanagementextender.wrapper.ServerManagementInitialized;
import net.minecraft.server.dedicated.management.dispatch.ManagementHandlerDispatcher;
import net.minecraft.server.dedicated.management.network.ManagementConnectionId;

@ServerManagementInitialized
public class ChatMessageMethod {
    static {
        RpcMethodBuilder.of(ManagementSchema.BOOLEAN)
                .responsePropertyName("success")
                .parameter(RpcChatMessage.SCHEMA)
                .parameterName("message")
                .description("Send a message on behalf of a player")
                .identifier("your_mod_id", "chat_message/send")
                .build(ChatMessageMethod::chatMessageHandler);
    }

    private static boolean chatMessageHandler(ManagementHandlerDispatcher d, RpcChatMessage message, ManagementConnectionId i) {
        // Your handler code here
    }
}
