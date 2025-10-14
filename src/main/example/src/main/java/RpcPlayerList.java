package dev.treset.servermanagementextender;

import dev.treset.servermanagementextender.wrapper.ManagementSchema;
import dev.treset.servermanagementextender.wrapper.ServerManagementInitialized;
import net.minecraft.server.dedicated.management.RpcPlayer;

import java.util.List;

@ServerManagementInitialized
public record RpcPlayerList(
        int playerCount,
        List<RpcPlayer> players
) {
    public static final ManagementSchema<RpcPlayerList> SCHEMA = ManagementSchema
            .<RpcPlayerList>builder("your_mod_id", "player_list")
            .property("count", ManagementSchema.INTEGER, RpcPlayerList::playerCount)
            .property("players", ManagementSchema.PLAYER.asList(), RpcPlayerList::players)
            .build(RpcPlayerList::new);
}
