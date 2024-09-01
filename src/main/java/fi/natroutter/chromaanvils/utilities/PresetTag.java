package fi.natroutter.chromaanvils.utilities;

import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.Modifying;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class PresetTag implements Modifying {

    public String content;

    @Override
    public Component apply(@NotNull Component current, int depth) {
        return Component.text(content, current.style());
    }
}
