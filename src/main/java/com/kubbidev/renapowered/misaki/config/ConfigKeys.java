package com.kubbidev.renapowered.misaki.config;

import com.kubbidev.javatoolbox.config.generic.KeyedConfiguration;
import com.kubbidev.javatoolbox.config.generic.key.ConfigKey;
import com.kubbidev.javatoolbox.config.generic.key.SimpleConfigKey;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import java.util.List;

import static com.kubbidev.javatoolbox.config.generic.key.ConfigKeyFactory.*;

public class ConfigKeys {
    private ConfigKeys() {}

    /**
     * Token used when set to release build.
     */
    public static final ConfigKey<String> TOKEN = notReloadable(stringKey("token", "00"));

    /**
     * Setting up the activity of the Bot.
     */
    public static final ConfigKey<String> PRESENCE_ACTIVITY = stringKey("presence.activity", "%guilds% Servers. (#%shard%)");

    /**
     * Setting up the status of the Bot.
     */
    public static final ConfigKey<OnlineStatus> PRESENCE_STATUS = enumKey("presence.status", OnlineStatus.ONLINE);

    /**
     * Setting up the type of presence for the Bot.
     */
    public static final ConfigKey<Activity.ActivityType> PRESENCE_TYPE = enumKey("presence.type", Activity.ActivityType.PLAYING);

    /**
     * Setting up the activity url of the Bot.
     */
    public static final ConfigKey<String> PRESENCE_URL = stringKey("presence.url", "https://twitch.tv/");

    /**
     * The shard amount of the bot. Check out <a href="https://anidiots.guide/understanding/sharding/#sharding">[Click to view]</a> for more information.
     */
    public static final ConfigKey<Integer> SHARDS = notReloadable(integerKey("shards", 1));

    /**
     * A list of the keys defined in this class.
     */
    private static final List<SimpleConfigKey<?>> KEYS = KeyedConfiguration.initialise(ConfigKeys.class);

    public static List<? extends ConfigKey<?>> getKeys() {
        return KEYS;
    }
}
