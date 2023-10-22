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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Locale;
import java.util.function.Consumer;

@CommandInfo(
        name = "kick",
        description = "misaki.usage.kick.description",
        category = Category.MOD
)
public class KickCommand implements Command<Misaki> {

    @Override
    public void onPerform(CommandManager<Misaki> commandManager, CommandEvent commandEvent) {

        Locale locale = commandEvent.getUserLocal();

        if (!commandEvent.getGuild().getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
            commandEvent.reply(TranslationManager.render("misaki.commandsystem.need-permission", locale,
                    "`" + Permission.KICK_MEMBERS + "`"),
                    5);
            return;
        }

        OptionMapping targetOption = commandEvent.getOption("target");
        OptionMapping reasonOption = commandEvent.getOption("reason");

        if (targetOption != null) {

            Member target = targetOption.getAsMember();
            String reason = reasonOption == null ? "No reason given!" : reasonOption.getAsString();

            kickMember(target, reason, commandEvent, locale);
        }
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandMeta -> {
            slashCommandMeta.defaultMemberPermissions(DefaultMemberPermissions.enabledFor(Permission.KICK_MEMBERS))
                    .options(new SlashOptionMeta("target", "misaki.usage.kick.argument.target", OptionType.USER, true))
                    .options(new SlashOptionMeta("reason", "misaki.usage.kick.argument.reason", OptionType.STRING));
        };
    }

    public void kickMember(Member member, String reason, CommandEvent commandEvent, Locale locale) {

        if (commandEvent.getGuild().getSelfMember().canInteract(member) && commandEvent.getMember().canInteract(member)) {
            commandEvent.getGuild().kick(member).reason(reason).queue();

            commandEvent.reply(TranslationManager.render("misaki.command.kick.success", locale,
                    "`" + member.getUser().getName() + "`"),
                    5);

        } else {
            if (commandEvent.getGuild().getSelfMember().canInteract(member))
                commandEvent.reply(TranslationManager.render("misaki.command.kick.hierarchy-self-error", locale),5);
            else commandEvent.reply(TranslationManager.render("misaki.command.kick.hierarchy-bot-error", locale),5);
        }
    }
}
