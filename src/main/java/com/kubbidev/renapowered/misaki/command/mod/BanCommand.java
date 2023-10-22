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
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@CommandInfo(
        name = "ban",
        description = "misaki.usage.ban.description",
        category = Category.MOD
)
public class BanCommand implements Command<Misaki> {

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
        OptionMapping deleteDaysOption = commandEvent.getOption("delete-days");
        OptionMapping reasonOption = commandEvent.getOption("reason");

        if (targetOption != null) {

            Member target = targetOption.getAsMember();
            String reason = reasonOption == null ? "No reason given!" : reasonOption.getAsString();
            int deleteDays = deleteDaysOption == null ? 0 : deleteDaysOption.getAsInt();

            banMember(target, deleteDays, reason, commandEvent, locale);
        }
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandMeta -> {
            slashCommandMeta.defaultMemberPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
                    .options(new SlashOptionMeta("target", "misaki.usage.ban.argument.target", OptionType.USER, true))
                    .options(new SlashOptionMeta("delete-days", "misaki.usage.ban.argument.delete-days", OptionType.INTEGER)
                            .requiredRange(0, 7))
                    .options(new SlashOptionMeta("reason", "misaki.usage.ban.argument.reason", OptionType.STRING));
        };
    }

    public void banMember(Member member, int deleteDays, String reason, CommandEvent commandEvent, Locale locale) {

        if (commandEvent.getGuild().getSelfMember().canInteract(member) && commandEvent.getMember().canInteract(member)) {
            commandEvent.getGuild().ban(member, deleteDays, TimeUnit.DAYS).reason(reason).queue();

            commandEvent.reply(TranslationManager.render("misaki.command.ban.success", locale,
                    "`" + member.getUser().getName() + "`"),
                    5);

        } else {
            if (commandEvent.getGuild().getSelfMember().canInteract(member))
                commandEvent.reply(TranslationManager.render("misaki.command.ban.hierarchy-self-error", locale),5);
            else commandEvent.reply(TranslationManager.render("misaki.command.ban.hierarchy-bot-error", locale),5);
        }
    }
}
