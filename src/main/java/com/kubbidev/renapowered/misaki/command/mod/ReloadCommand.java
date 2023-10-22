package com.kubbidev.renapowered.misaki.command.mod;

import com.kubbidev.renapowered.RenaPowered;
import com.kubbidev.renapowered.command.Category;
import com.kubbidev.renapowered.command.CommandEvent;
import com.kubbidev.renapowered.command.CommandManager;
import com.kubbidev.renapowered.command.interfaces.Command;
import com.kubbidev.renapowered.command.interfaces.CommandInfo;
import com.kubbidev.renapowered.command.meta.SlashCommandMeta;
import com.kubbidev.renapowered.locale.TranslationManager;
import com.kubbidev.renapowered.misaki.Misaki;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;

import java.util.function.Consumer;

@CommandInfo(
        name = "reload",
        description = "misaki.usage.reload.description",
        category = Category.MOD
)
public class ReloadCommand implements Command<Misaki> {

    @Override
    public void onPerform(CommandManager<Misaki> commandManager, CommandEvent commandEvent) {
        RenaPowered<?> instance = commandManager.getInstance();

        instance.reload();
        commandEvent.reply(TranslationManager.render("misaki.command.reload.success", commandEvent.getUserLocal()));
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandData -> {
            slashCommandData.defaultMemberPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER));
        };
    }
}
