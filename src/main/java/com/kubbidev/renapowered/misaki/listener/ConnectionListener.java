package com.kubbidev.renapowered.misaki.listener;

import com.kubbidev.javatoolbox.logging.LoggerAdapter;
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ConnectionListener extends ListenerAdapter {

    private final LoggerAdapter logger;

    public ConnectionListener(LoggerAdapter logger) {
        this.logger = logger;
    }

    @Override
    public void onSessionDisconnect(@NotNull SessionDisconnectEvent event) {
        this.logger.info("Got disconnected on shard {}. Resume connection...", event.getJDA().getShardInfo().getShardId());
    }

    @Override
    public void onSessionResume(@NotNull SessionResumeEvent event) {
        this.logger.info("Connection successfully resumed for shard {}!", event.getJDA().getShardInfo().getShardId());
    }
}
