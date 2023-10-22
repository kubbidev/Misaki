package com.kubbidev.renapowered.misaki.command.mod;

import com.kubbidev.renapowered.command.Category;
import com.kubbidev.renapowered.command.CommandEvent;
import com.kubbidev.renapowered.command.CommandManager;
import com.kubbidev.renapowered.command.interfaces.Command;
import com.kubbidev.renapowered.command.interfaces.CommandInfo;
import com.kubbidev.renapowered.command.meta.SlashCommandMeta;
import com.kubbidev.renapowered.command.meta.SlashOptionMeta;
import com.kubbidev.renapowered.locale.TranslationManager;
import com.kubbidev.renapowered.misaki.Misaki;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Locale;
import java.util.function.Consumer;

@CommandInfo(
        name = "unban",
        description = "misaki.usage.unban.description",
        category = Category.MOD
)
public class UnbanCommand implements Command<Misaki> {

    @Override
    public void onPerform(CommandManager<Misaki> commandManager, CommandEvent commandEvent) {

        Locale locale = commandEvent.getUserLocal();

        if (!commandEvent.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
            commandEvent.reply(TranslationManager.render("misaki.commandsystem.need-permission", locale,
                    "`" + Permission.BAN_MEMBERS + "`"),
                    5);
            return;
        }

        OptionMapping targetOption = commandEvent.getOption("target");

        if (targetOption != null) {
            try {
                commandEvent.getGuild().unban(UserSnowflake.fromId(targetOption.getAsString())).queue(u -> {
                    commandEvent.reply(TranslationManager.render("misaki.command.unban.success", locale,
                                    "<@" + targetOption.getAsString() + ">"),
                            5);
                }, t -> {
                    printUserException(commandEvent, locale, targetOption);
                });
            } catch (Exception ignored) {
                printUserException(commandEvent, locale, targetOption);
            }
        }
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandMeta -> {
            slashCommandMeta.defaultMemberPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                    .options(new SlashOptionMeta("target", "misaki.usage.unban.argument.target", OptionType.STRING, true));
        };
    }

    private void printUserException(CommandEvent commandEvent, Locale locale, OptionMapping targetOption) {
        commandEvent.reply(TranslationManager.render("misaki.command.unban.not-found", locale,
                        "`" + targetOption.getAsString() + "`"),
                5);
    }
}
