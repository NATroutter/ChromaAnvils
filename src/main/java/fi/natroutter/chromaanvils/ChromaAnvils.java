package fi.natroutter.chromaanvils;

import fi.natroutter.chromaanvils.config.ModConfig;
import fi.natroutter.chromaanvils.utilities.Colors;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;

public class ChromaAnvils implements ModInitializer {

    public static final String MOD_ID = "chromaanvils";

    private static ConfigHolder<ModConfig> config;
    public static ModConfig config() {
        return config.get();
    }

    @Override
    public void onInitialize() {

        config = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Colors.serverAudiences = MinecraftServerAudiences.of(server);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            Colors.serverAudiences = null;
        });
    }
}