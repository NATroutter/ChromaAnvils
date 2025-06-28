package fi.natroutter.chromaanvils.utilities;

import fi.natroutter.chromaanvils.ChromaAnvils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    public static boolean hasPermission(PlayerEntity player, String perm, boolean defaultValue) {
        if (!ChromaAnvils.config().UsePermissions) {
            return true;
        }
        if (!(player instanceof ServerPlayerEntity)) {
            return true;
        }
        return Permissions.check(player, ChromaAnvils.MOD_ID + "." + perm, defaultValue);
    }

    public static TagResolver[] GetTagsFromPlayerPermissions(ServerPlayerEntity player) {
        ArrayList<TagResolver> tags = new ArrayList<TagResolver>();
        if (hasPermission(player, "colors", false)) tags.add(StandardTags.color());
        if (hasPermission(player, "decorations", false)) tags.add(StandardTags.decorations());
        if (hasPermission(player, "font", false)) tags.add(StandardTags.font());
        if (hasPermission(player, "gradient", false)) tags.add(StandardTags.gradient());
        if (hasPermission(player, "rainbow", false)) tags.add(StandardTags.rainbow());
        if (hasPermission(player, "transition", false)) tags.add(StandardTags.transition());
        if (hasPermission(player, "reset", false)) tags.add(StandardTags.reset());
        if (hasPermission(player, "shadow", false)) tags.add(StandardTags.shadowColor());
        if (hasPermission(player, "pride", false)) tags.add(StandardTags.pride());
        return tags.toArray(new TagResolver[0]);
    }

    public static String extractWithTags(String input, int amount) {
        StringBuilder result = new StringBuilder();
        int visibleCharCount = 0;

        // Regex pattern to match both tags and regular text
        Pattern pattern = Pattern.compile("<[^>]+>|[^<]+");
        Matcher matcher = pattern.matcher(input);

        // Process each match (either tag or regular text)
        while (matcher.find()) {
            String match = matcher.group();

            // If it's a tag, add it fully to the result
            if (match.startsWith("<") && match.endsWith(">")) {
                result.append(match);
            }
            // If it's regular text, check if adding it exceeds the allowed length
            else {
                for (char c : match.toCharArray()) {
                    if (visibleCharCount < amount) {
                        result.append(c);
                        visibleCharCount++;
                    } else {
                        break;
                    }
                }
            }

            // If we reached the desired length, stop processing
            if (visibleCharCount >= amount) {
                break;
            }
        }

        return result.toString();
    }

    public static String extractWithTags2(String input, int amount) {
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("<[^>]+>|[^<]+");
        Matcher matcher = pattern.matcher(input);
        int count = 0;

        while (matcher.find() && count < amount) {
            String match = matcher.group();
            if (match.startsWith("<")) {
                String plain = Colors.plain(Colors.deserialize(match));
                if (!plain.isEmpty()) {
                    continue;
                }
                result.append(match);
            } else {
                if (match.endsWith("\\")) {
                    match = match.replace("\\", "");
                }
                if (count + match.length() <= amount) {
                    result.append(match);
                    count += match.length();
                } else {
                    result.append(match, 0, amount - count);
                    count = amount;
                }
            }
        }

        return result.toString();
    }
}
