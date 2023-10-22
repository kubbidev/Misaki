package com.kubbidev.renapowered.misaki;

import com.google.common.collect.ImmutableSet;
import com.kubbidev.javatoolbox.config.generic.adapter.ConfigurationAdapter;
import com.kubbidev.javatoolbox.config.generic.key.ConfigKey;
import com.kubbidev.renapowered.Plugin;
import com.kubbidev.renapowered.PluginDescription;
import com.kubbidev.renapowered.RenaConfigAdapter;
import com.kubbidev.renapowered.RenaPowered;
import com.kubbidev.renapowered.command.interfaces.Command;
import com.kubbidev.renapowered.misaki.command.fun.AvatarCommand;
import com.kubbidev.renapowered.misaki.command.fun.PingCommand;
import com.kubbidev.renapowered.misaki.command.info.AboutCommand;
import com.kubbidev.renapowered.misaki.command.info.InviteCommand;
import com.kubbidev.renapowered.misaki.command.mod.*;
import com.kubbidev.renapowered.misaki.config.ConfigKeys;
import com.kubbidev.renapowered.misaki.listener.ConnectionListener;
import com.kubbidev.renapowered.misaki.listener.GuildListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

@Plugin(
        id = "renapowered-misaki",
        name = "Misaki",
        version = "1.0.0",
        description = "Misaki is a simple but powerful Discord bot used to enhance your Discord experience.",
        url = "https://discord.kubbidev.com/",
        authors = "kubbidev"
)
public class Misaki extends RenaPowered<Misaki> {

    public static Misaki INSTANCE;

    /**
     * Main method that the JVM will call when {@code java -jar misaki.jar} is executed.
     *
     * @param args the arguments to the bot
     */
    public static void main(String[] args) {
        RenaPowered.loadInstance(Misaki.class);
    }

    public Misaki(@NotNull PluginDescription description) {
        super(description);
    }

    @Override
    public void reload() {
        super.reload();

        /*
            Reload the bot presence activity after
            the config file was reloaded.
         */
        reloadPresence(null);
    }

    @Override
    protected void load() {
        INSTANCE = this;
    }

    @Override
    protected void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    protected void ready(@NotNull JDA discordJDA) {
        reloadPresence(discordJDA);
    }

    @Override
    public void registerListeners(@NotNull Set<ListenerAdapter> listeners) {
        listeners.add(new ConnectionListener(getLogger()));
        listeners.add(new GuildListener(getLogger()));
    }

    @Override
    protected @NotNull ConfigurationAdapter provideConfigurationAdapter() {
        return new RenaConfigAdapter(resolvePath("config.yml"));
    }

    @Override
    protected @NotNull List<? extends ConfigKey<?>> provideConfigKeys() {
        return ConfigKeys.getKeys();
    }

    @Override
    protected @NotNull ConfigKey<String> provideToken() {
        return ConfigKeys.TOKEN;
    }

    @Override
    protected @NotNull ConfigKey<Integer> provideShards() {
        return ConfigKeys.SHARDS;
    }

    @Override
    public @NotNull Set<Class<? extends Command<Misaki>>> provideCommands() {
        return ImmutableSet.of(
                ReloadCommand.class,
                ClearCommand.class,
                AvatarCommand.class,
                AboutCommand.class,
                BanCommand.class,
                KickCommand.class,
                UnbanCommand.class,
                PingCommand.class,
                InviteCommand.class
        );
    }

    @Override
    public @NotNull Type getType() {
        return Type.STANDALONE;
    }

    /**
     * Reloads the bot activity presence status & message.
     *
     * @param discordJDA to reload the presence
     */
    public void reloadPresence(@Nullable JDA discordJDA) {
        if (discordJDA == null) {
            getManager().setActivity(
                    getConfiguration().get(ConfigKeys.PRESENCE_STATUS),
                    getConfiguration().get(ConfigKeys.PRESENCE_ACTIVITY),
                    getConfiguration().get(ConfigKeys.PRESENCE_TYPE),
                    getConfiguration().get(ConfigKeys.PRESENCE_URL)
            );
        } else {
            getManager().setActivity(discordJDA,
                    getConfiguration().get(ConfigKeys.PRESENCE_STATUS),
                    getConfiguration().get(ConfigKeys.PRESENCE_ACTIVITY),
                    getConfiguration().get(ConfigKeys.PRESENCE_TYPE),
                    getConfiguration().get(ConfigKeys.PRESENCE_URL)
            );
        }
    }
}
