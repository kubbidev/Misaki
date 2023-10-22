package com.kubbidev.renapowered.misaki.command.fun;

import com.kubbidev.renapowered.command.Category;
import com.kubbidev.renapowered.command.CommandEvent;
import com.kubbidev.renapowered.command.CommandManager;
import com.kubbidev.renapowered.command.interfaces.Command;
import com.kubbidev.renapowered.command.interfaces.CommandInfo;
import com.kubbidev.renapowered.command.meta.SlashCommandMeta;
import com.kubbidev.renapowered.locale.TranslationManager;
import com.kubbidev.renapowered.misaki.Misaki;

import java.util.function.Consumer;

@CommandInfo(
        name = "ping",
        description = "misaki.usage.ping.description",
        category = Category.FUN
)
public class PingCommand implements Command<Misaki> {

    @Override
    public void onPerform(CommandManager<Misaki> commandManager, CommandEvent commandEvent) {
        commandEvent.reply(TranslationManager.render("misaki.command.ping", commandEvent.getUserLocal(), "\uD83C\uDFD3"), 5);
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandMeta -> {

        };
    }
}
