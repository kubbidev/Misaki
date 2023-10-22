package com.kubbidev.renapowered.misaki.command.fun;

import com.kubbidev.renapowered.misaki.Misaki;
import com.kubbidev.renapowered.misaki.ShardConstant;
import com.kubbidev.renapowered.command.Category;
import com.kubbidev.renapowered.command.CommandEvent;
import com.kubbidev.renapowered.command.CommandManager;
import com.kubbidev.renapowered.command.interfaces.Command;
import com.kubbidev.renapowered.command.interfaces.CommandInfo;
import com.kubbidev.renapowered.command.meta.SlashCommandMeta;
import com.kubbidev.renapowered.command.meta.SlashOptionMeta;
import com.kubbidev.renapowered.locale.TranslationManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.function.Consumer;

@CommandInfo(
        name = "avatar",
        description = "misaki.usage.avatar.description",
        category = Category.FUN
)
public class AvatarCommand implements Command<Misaki> {

    @Override
    public void onPerform(CommandManager<Misaki> commandManager, CommandEvent commandEvent) {
        OptionMapping amountOption = commandEvent.getOption("user");

        if (amountOption != null) {
            processEmbed(commandManager, commandEvent, amountOption.getAsUser());
        }
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandMeta -> {
            slashCommandMeta.options(new SlashOptionMeta("user", "misaki.usage.avatar.argument.user", OptionType.USER)
                    .required(true));
        };
    }

    private void processEmbed(CommandManager<?> commandManager, CommandEvent commandEvent, User user) {

        String iconUrl = user.getEffectiveAvatarUrl();
        Locale locale = commandEvent.getUserLocal();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(TranslationManager.render("misaki.command.avatar.embed-title", locale, user.getEffectiveName()),
                        iconUrl)

                .setFooter(TranslationManager.render("misaki.embedsystem.requested-footer", locale, commandEvent.getUser().getEffectiveName()),
                        commandEvent.getUser().getEffectiveAvatarUrl())

                .setImage(iconUrl)
                .setTimestamp(ZonedDateTime.now())
                .setColor(ShardConstant.DEFAULT_COLOR);

        // send embed in text channel (no user only message)
        commandEvent.delete();
        commandManager.sendMessage(embedBuilder, commandEvent.getChannel());
    }
}