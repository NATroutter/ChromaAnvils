package fi.natroutter.chromaanvils.utilities;

import fi.natroutter.chromaanvils.ChromaAnvils;
import fi.natroutter.chromaanvils.config.ModConfig;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.platform.fabric.FabricClientAudiences;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.intellij.lang.annotations.Subst;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class Colors {

    public static volatile FabricAudiences audiences;

    public static MiniMessage miniMessage() {
        TagResolver.Builder trb = TagResolver.builder()
                .resolver(StandardTags.color())
                .resolver(StandardTags.decorations())
                .resolver(StandardTags.font())
                .resolver(StandardTags.gradient())
                .resolver(StandardTags.rainbow())
                .resolver(StandardTags.transition())
                .resolver(StandardTags.reset());

        if (ChromaAnvils.config().PresetsEnabled) {

            ChromaAnvils.config().Presets.stream()
                    .filter(Utils.distinctByKey(e->e.tag))
                    .forEach(e-> {
                        trb.resolver(TagResolver.resolver(e.tag, new PresetTag(e.content)));
                        System.out.println("Debug: " + e.tag + " - " + e.content);
                    });

        }

        MiniMessage.Builder mmb = MiniMessage.builder().tags(trb.build());

        return mmb.build();
    }

    public static FabricAudiences getAudience() {
        //throw new IllegalStateException("Tried to access Adventure without a running server!");
        if (audiences == null) return null;
        return audiences;
    }

    public static String plain(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String serialize(Component component) {
        return miniMessage().serialize(component);
    }

    public static Component deserialize(String value) {
        return miniMessage().deserialize(value);
    }

    public static Text toNative(Component component) {
        if (getAudience() == null) return null;
        return getAudience().toNative(component);
    }

}
