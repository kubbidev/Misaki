package com.kubbidev.renapowered.misaki.command.info;

import com.kubbidev.renapowered.misaki.Misaki;
import com.kubbidev.renapowered.misaki.ShardConstant;
import com.kubbidev.renapowered.command.Category;
import com.kubbidev.renapowered.command.CommandEvent;
import com.kubbidev.renapowered.command.CommandManager;
import com.kubbidev.renapowered.command.interfaces.Command;
import com.kubbidev.renapowered.command.interfaces.CommandInfo;
import com.kubbidev.renapowered.command.meta.SlashCommandMeta;
import com.kubbidev.renapowered.locale.TranslationManager;
import com.kubbidev.renapowered.util.duration.DurationFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.function.Consumer;

@CommandInfo(
        name = "about",
        description = "misaki.usage.about.description",
        category = Category.INFO
)
public class AboutCommand implements Command<Misaki> {

    @Override
    public void onPerform(CommandManager<Misaki> commandManager, CommandEvent commandEvent) {

        User author = commandEvent.getUser();
        JDA discordJDA = author.getJDA();

        Instant startTime = commandManager.getInstance().getStartTime();
        Locale locale = commandEvent.getUserLocal();

        ShardManager shardManager = commandManager.getInstance().getManager().getShardManager();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(TranslationManager.render("misaki.command.about.embed-author", locale))
                .setTimestamp(ZonedDateTime.now())
                .addField(
                        TranslationManager.render("misaki.command.about.embed-field.total-title", locale),
                        TranslationManager.render("misaki.command.about.embed-field.total-value", locale,
                                shardManager.getShardCache().size(),
                                shardManager.getGuildCache().size()),
                        false
                )
                .addField(
                        TranslationManager.render("misaki.command.about.embed-field.this-title", locale),
                        TranslationManager.render("misaki.command.about.embed-field.this-value", locale,
                                discordJDA.getShardInfo().getShardId(),
                                discordJDA.getGuildCache().size()),
                        false
                )
                .addField(
                        TranslationManager.render("misaki.command.about.embed-field.other-title", locale),
                        TranslationManager.render("misaki.command.about.embed-field.other-value", locale,
                                ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() >> 20,
                                ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() >> 20,
                                DurationFormatter.LONG.format(Duration.between(startTime, Instant.now()), locale)),
                        false
                )
                .setColor(ShardConstant.DEFAULT_COLOR)
                .setFooter(TranslationManager.render("misaki.embedsystem.requested-footer", locale,
                                author.getEffectiveName()),
                        author.getEffectiveAvatarUrl());

        // send embed in text channel (no user only message)
        commandEvent.delete();
        commandManager.sendMessage(embedBuilder, commandEvent.getChannel());
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandMeta -> {

        };
    }
}
