package com.kubbidev.renapowered.misaki.command.info;

import com.kubbidev.renapowered.command.Category;
import com.kubbidev.renapowered.command.CommandEvent;
import com.kubbidev.renapowered.command.CommandManager;
import com.kubbidev.renapowered.command.interfaces.Command;
import com.kubbidev.renapowered.command.interfaces.CommandInfo;
import com.kubbidev.renapowered.command.meta.SlashCommandMeta;
import com.kubbidev.renapowered.locale.TranslationManager;
import com.kubbidev.renapowered.misaki.Misaki;
import com.kubbidev.renapowered.misaki.ShardConstant;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.function.Consumer;

@CommandInfo(
        name = "invite",
        description = "misaki.usage.invite.description",
        category = Category.INFO
)
public class InviteCommand implements Command<Misaki> {

    @Override
    public void onPerform(CommandManager<Misaki> commandManager, CommandEvent commandEvent) {

        User user = commandEvent.getUser();
        Guild guild = commandEvent.getGuild();

        String iconUrl = guild.getIconUrl();
        String bannerUrl = guild.getBannerUrl();

        String title = guild.getName();
        String desc = guild.getDescription();

        Locale locale = commandEvent.getUserLocal();

        MessageCreateBuilder builder = new MessageCreateBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("**")
                .append(TranslationManager.render("misaki.command.invite.label.description", locale))
                .append(" :**");

        stringBuilder.append('\n');
        stringBuilder.append(desc);
        stringBuilder.append('\n');
        stringBuilder.append('\n');
        stringBuilder.append("**")
                .append(TranslationManager.render("misaki.command.invite.label.invite-link", locale))
                .append(" :**")
                .append(' ');
        stringBuilder.append(Misaki.INSTANCE.getUrl().orElse("https://discord.gg/"));

        builder.addEmbeds(new EmbedBuilder()
                .setTitle(title)
                .setThumbnail(iconUrl)
                .setImage(bannerUrl)
                .setDescription(stringBuilder)

                .setFooter(TranslationManager.render("misaki.embedsystem.requested-footer", locale, user.getEffectiveName()),
                        user.getEffectiveAvatarUrl())

                .setTimestamp(ZonedDateTime.now())
                .setColor(ShardConstant.DEFAULT_COLOR)
                .build());

        builder.addActionRow(Button.of(
                ButtonStyle.LINK,
                Misaki.INSTANCE.getUrl().orElse("https://discord.gg/"),
                TranslationManager.render("misaki.command.invite.label.invite", locale),
                Emoji.fromUnicode("\uD83D\uDCE9")
        ));

        // send embed in text channel (no user only message)
        commandEvent.delete();
        commandManager.sendMessage(builder.build(), commandEvent.getChannel());
    }

    @Override
    public Consumer<SlashCommandMeta> getCommandData() {
        return slashCommandMeta -> {

        };
    }
}
