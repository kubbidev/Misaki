package com.kubbidev.renapowered.misaki.listener;

import com.kubbidev.javatoolbox.logging.LoggerAdapter;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildListener extends ListenerAdapter {

    private final LoggerAdapter logger;

    public GuildListener(LoggerAdapter logger) {
        this.logger = logger;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        this.logger.info("[Guild Join] {} (id: {}, members: {})",

                event.getGuild().getName(),
                event.getGuild().getId(),
                event.getGuild().getMemberCount());
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        this.logger.info("[Guild Leave] {} (id: {}, members: {})",

                event.getGuild().getName(),
                event.getGuild().getId(),
                event.getGuild().getMemberCount());
    }
}
