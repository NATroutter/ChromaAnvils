package fi.natroutter.chromaanvils.config;

import fi.natroutter.chromaanvils.ChromaAnvils;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;


import java.util.ArrayList;
import java.util.List;

@Config(name = ChromaAnvils.MOD_ID)
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public int AnvilTextLimit = 255;

    @ConfigEntry.Gui.Tooltip
    public int NameLimit = 50;

    @ConfigEntry.Gui.Tooltip
    public boolean UsePermissions = false;

    @ConfigEntry.Gui.Tooltip
    public boolean DisableTooExpensiveLimit = false;

    @ConfigEntry.Gui.Tooltip
    public List<String> BlackListedItems = new ArrayList<>(List.of("minecraft:name_tag <-example"));


    // new method for new mappings
    public boolean isBlacklisted(ItemStack stack) {
        Identifier itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return BlackListedItems.stream().anyMatch(item -> item.equalsIgnoreCase(itemId.toString()));
    }

}
