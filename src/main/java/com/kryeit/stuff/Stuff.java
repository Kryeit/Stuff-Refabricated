package com.kryeit.stuff;

import com.kryeit.stuff.command.*;
import com.kryeit.stuff.listener.PlayerDeath;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import java.util.HashMap;
import java.util.UUID;

public class Stuff implements DedicatedServerModInitializer {

    //public static Queue queue = new Queue();
    public static HashMap<UUID, Long> lastActiveTime = new HashMap<>();

    @Override
    public void onInitializeServer() {
        registerEvents();
        registerCommands();
    }

    public void registerEvents() {
        //ServerPlayConnectionEvents.INIT.register(new QueueHandler());
        ServerLivingEntityEvents.AFTER_DEATH.register(new PlayerDeath());
    }

    public void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicatedServer, commandFunction) -> {
            Discord.register(dispatcher);
            Kofi.register(dispatcher);
            Map.register(dispatcher);
            Rules.register(dispatcher);
            Vote.register(dispatcher);
            SendCoords.register(dispatcher);
            Reply.register(dispatcher);
            Message.register(dispatcher);
            TPS.register(dispatcher);
        });
    }
}
