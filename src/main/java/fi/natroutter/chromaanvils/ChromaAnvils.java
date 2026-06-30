package fi.natroutter.chromaanvils;

import fi.natroutter.chromaanvils.config.ModConfig;
import fi.natroutter.chromaanvils.network.ConfigSyncPayload;
import fi.natroutter.chromaanvils.utilities.Colors;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;
import net.minecraft.server.MinecraftServer;

public class ChromaAnvils implements ModInitializer {

    public static final String MOD_ID = "chromaanvils";

    private static ConfigHolder<ModConfig> config;
    private static MinecraftServer server;
    private static volatile Boolean syncedDisableTooExpensiveLimit;

    public static ModConfig config() {
        return config.get();
    }

    public static boolean disableTooExpensiveLimit() {
        if (syncedDisableTooExpensiveLimit != null) {
            return syncedDisableTooExpensiveLimit;
        }

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && server == null) {
            return false;
        }

        return config().DisableTooExpensiveLimit;
    }

    public static void setSyncedDisableTooExpensiveLimit(boolean value) {
        syncedDisableTooExpensiveLimit = value;
    }

    public static void clearSyncedServerConfig() {
        syncedDisableTooExpensiveLimit = null;
    }

    public static boolean isDedicatedServer() {
        return server == null || server.isDedicatedServer();
    }

    @Override
    public void onInitialize() {

        config = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);

        PayloadTypeRegistry.clientboundPlay().register(ConfigSyncPayload.TYPE, ConfigSyncPayload.STREAM_CODEC);

        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            if (ServerPlayNetworking.canSend(listener, ConfigSyncPayload.TYPE)) {
                ServerPlayNetworking.send(listener.getPlayer(), new ConfigSyncPayload(config().DisableTooExpensiveLimit));
            }
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            ChromaAnvils.server = server;
            Colors.serverAudiences = MinecraftServerAudiences.of(server);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            ChromaAnvils.server = null;
            Colors.serverAudiences = null;
        });
    }
}
