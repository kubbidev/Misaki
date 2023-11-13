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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.data.DataObject;

import java.util.Locale;
import java.util.function.Consumer;

@CommandInfo(
        name = "embed",
        description = "misaki.usage.embed.description",
        category = Category.MOD
)
public class EmbedCommand implements Command<Misaki> {

    @Override
    public void onPerform(CommandManager<Misaki> commandManager, CommandEvent commandEvent) {

        Locale locale = commandEvent.getUserLocal();

        if (!commandEvent.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            commandEvent.reply(TranslationManager.render("misaki.commandsystem.need-permission", locale,
                            "`" + Permission.MESSAGE_MANAGE + "`"),
                    5);
            return;
        }

        OptionMapping jsonOption = commandEvent.getOption("json");

        if (jsonOption != null) {
            postMessages(commandManager, commandEvent, locale, jsonOption.getAsString());
        }
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandMeta -> {
            slashCommandMeta.defaultMemberPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE))
                    .options(new SlashOptionMeta("json", "misaki.usage.embed.argument.json", OptionType.STRING, true));
        };
    }

    private void postMessages(CommandManager<?> commandManager, CommandEvent commandEvent, Locale locale, String json) {
        try {
            DataObject dataObject = DataObject.fromJson(json);
            EmbedBuilder embedBuilder = EmbedBuilder.fromData(dataObject);

            // send embed in text channel (no user only message)
            commandEvent.delete();
            commandManager.sendMessage(embedBuilder, commandEvent.getChannel());

        } catch (Exception e) {
            commandEvent.reply(TranslationManager.render("misaki.command.embed.error-parsing", locale),5);
        }
    }
}
