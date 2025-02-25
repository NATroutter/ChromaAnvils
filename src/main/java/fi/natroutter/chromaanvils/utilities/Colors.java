package fi.natroutter.chromaanvils.utilities;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver.Builder;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.text.Text;

public class Colors {

    public static volatile MinecraftServerAudiences serverAudiences;

    public static MiniMessage miniMessage() {
        return MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.font())
                        .resolver(StandardTags.gradient())
                        .resolver(StandardTags.rainbow())
                        .resolver(StandardTags.transition())
                        .resolver(StandardTags.reset())
                        .build()
                ).build();
    }
    public static MiniMessage miniMessage(TagResolver[] tags) {
        Builder tagBuilder = TagResolver.builder();
        for (TagResolver tag : tags) {
            tagBuilder.resolver(tag);
        }
        TagResolver tagResolver = tagBuilder.build();

        return MiniMessage.builder().tags(tagResolver).build();
    }

    public static MinecraftServerAudiences getServerAudience() {
        if (serverAudiences == null) return null;
        return serverAudiences;
    }
    public static MinecraftClientAudiences getClientAudience() {
        return MinecraftClientAudiences.of();
    }

    public static String plain(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String serialize(Component component) {
        return miniMessage().serialize(component);
    }

    public static Component deserialize(String value) {
        // for normal handler
        return miniMessage().deserialize(value);
    }

    public static Component deserialize(String value, TagResolver[] tags) {
        // for server handler
        return miniMessage(tags).deserialize(value);
    }

    public static Text toNative(Component component) {
        if (getServerAudience() != null) {
            return getServerAudience().asNative(component);
        }
        return getClientAudience().asNative(component);
    }

    public static Component toAdventure(Text text) {
        if (getServerAudience() != null) {
            return getServerAudience().asAdventure(text);
        }
        return getClientAudience().asAdventure(text);
    }

}
