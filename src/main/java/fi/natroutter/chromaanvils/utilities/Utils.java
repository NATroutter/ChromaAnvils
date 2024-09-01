package fi.natroutter.chromaanvils.utilities;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> key) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(key.apply(t), Boolean.TRUE) == null;
    }

    public static String extractWithTags(String input, int amount) {
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
