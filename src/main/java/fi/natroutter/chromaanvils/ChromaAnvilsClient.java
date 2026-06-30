package fi.natroutter.chromaanvils;

import fi.natroutter.chromaanvils.network.ConfigSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ChromaAnvilsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.TYPE, (payload, context) -> {
            ChromaAnvils.setSyncedDisableTooExpensiveLimit(payload.disableTooExpensiveLimit());
        });

        ClientPlayConnectionEvents.DISCONNECT.register((listener, client) -> {
            ChromaAnvils.clearSyncedServerConfig();
        });
    }
}
