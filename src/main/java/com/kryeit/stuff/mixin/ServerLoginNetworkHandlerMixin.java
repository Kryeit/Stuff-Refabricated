package com.kryeit.stuff.mixin;

import com.kryeit.stuff.MinecraftServerSupplier;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.UUID;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {

    @Shadow @Nullable private GameProfile profile;

    @Inject(at = @At("RETURN"), method = "acceptPlayer")
    private void init(CallbackInfo ci) {
        UUID id = this.profile.getId();
        String name = this.profile.getName();

        ServerPlayerEntity player = MinecraftServerSupplier.getServer().getPlayerManager().getPlayer(id);
        File playerDataDirectory = new File("world/playerdata/");

        File[] playerDataFiles = playerDataDirectory.listFiles();

        if (playerDataFiles == null) return;

        for (File playerDataFile : playerDataFiles) {
            String fileName = playerDataFile.getName();
            if (!fileName.endsWith(".dat")) continue;
            UUID otherId = UUID.fromString(fileName.substring(0, fileName.length() - 4));
            if (id.equals(otherId)) {
                // Has joined before
                return;
            }
        }

        // Has NOT joined before
        MinecraftServerSupplier.getServer().getPlayerManager().broadcast(
                Text.literal("Welcome " + name + " to Kryeit!").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                false
        );

        assert player != null;
        player.sendMessage(Text.literal("Kryeit if fairly vanilla, but it has custom systems:").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
        player.sendMessage(Text.literal(" - Claim system (use /claim)").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
        player.sendMessage(Text.literal(" - Mission system (use /missions)").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
        player.sendMessage(Text.literal(" - Teleport system (use /post)").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
        player.sendMessage(Text.literal("For more information: https://kryeit.com/discord")
                .setStyle(Style.EMPTY.withColor(Formatting.AQUA))
                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://kryeit.com/discord"))));
        player.sendMessage(Text.literal("Read the /rules and have fun!").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
    }
}
