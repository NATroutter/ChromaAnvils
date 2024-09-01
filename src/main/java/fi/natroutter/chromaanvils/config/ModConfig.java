package fi.natroutter.chromaanvils.config;

import fi.natroutter.chromaanvils.ChromaAnvils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Config(name = ChromaAnvils.MOD_ID)
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public int AnvilTextLimit = 255;

    @ConfigEntry.Gui.Tooltip
    public int NameLimit = 50;

    @ConfigEntry.Gui.Tooltip
    public List<String> BlackListedItems = new ArrayList<>(List.of("minecraft:name_tag <-example"));

    @ConfigEntry.Gui.Tooltip
    public boolean PresetsEnabled = true;

    @ConfigEntry.Gui.Tooltip
    public List<Preset> Presets = new ArrayList<>();

    @AllArgsConstructor
    public static class Preset {
        @ConfigEntry.Gui.Tooltip
        public String tag;
        @ConfigEntry.Gui.Tooltip
        public String content;
    }

    public boolean isBlacklisted(ItemStack stack) {
        return BlackListedItems.stream().anyMatch(blacklistedId -> blacklistedId.equalsIgnoreCase(stack.getRegistryEntry().getIdAsString()));
    }

}