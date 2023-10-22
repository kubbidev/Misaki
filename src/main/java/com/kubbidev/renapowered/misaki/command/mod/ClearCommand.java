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
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Locale;
import java.util.function.Consumer;

@CommandInfo(
        name = "clear",
        description = "misaki.usage.clear.description",
        category = Category.MOD
)
public class ClearCommand implements Command<Misaki> {

    @Override
    public void onPerform(CommandManager<Misaki> commandManager, CommandEvent commandEvent) {

        Locale locale = commandEvent.getUserLocal();

        if (!commandEvent.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            commandEvent.reply(TranslationManager.render("misaki.commandsystem.need-permission", locale,
                    "`" + Permission.MESSAGE_MANAGE + "`"),
                    5);
            return;
        }

        OptionMapping amountOption = commandEvent.getOption("amount");

        if (amountOption != null) {
            deleteMessages(commandEvent, locale, amountOption.getAsInt());
        }
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandData -> {
            slashCommandData.options(new SlashOptionMeta("amount", "misaki.usage.clear.argument.amount", OptionType.INTEGER)
                            .required(true)
                            .minValue(2)
                            .maxValue(200))
                    .defaultMemberPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE));
        };
    }

    private void deleteMessages(CommandEvent commandEvent, Locale locale, int amount) {

        commandEvent.getChannel().getIterableHistory().takeAsync(amount).thenAccept(messages -> {
            commandEvent.getChannel().purgeMessages(messages);
            commandEvent.reply(TranslationManager.render("misaki.command.clear.success", locale,
                    "`" + amount + "`"),
                    5);

        }).exceptionally(throwable -> {

            commandEvent.reply(TranslationManager.render("misaki.commandsystem.error-executing", locale,
                    "`" + throwable.getMessage() + "`"),
                    5);
            return null;
        });
    }
}
