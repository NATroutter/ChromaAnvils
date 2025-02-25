package fi.natroutter.chromaanvils.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

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
