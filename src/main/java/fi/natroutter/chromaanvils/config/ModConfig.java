package fi.natroutter.chromaanvils.config;

import fi.natroutter.chromaanvils.ChromaAnvils;
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
    public boolean UsePermissions = true;

    @ConfigEntry.Gui.Tooltip
    public List<String> BlackListedItems = new ArrayList<>(List.of("minecraft:name_tag <-example"));


    public boolean isBlacklisted(ItemStack stack) {
        return BlackListedItems.stream().anyMatch(blacklistedId -> blacklistedId.equalsIgnoreCase(stack.getRegistryEntry().getIdAsString()));
    }

}