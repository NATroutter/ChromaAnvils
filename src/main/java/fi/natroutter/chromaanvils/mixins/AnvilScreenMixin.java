package fi.natroutter.chromaanvils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import fi.natroutter.chromaanvils.ChromaAnvils;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {

    @Shadow
    private EditBox name;

    @Shadow
    @Final
    private Player player;


    @Inject(method = "subInit", at = @At("TAIL"))
    public void setup(CallbackInfo ci) {
        name.setMaxLength(ChromaAnvils.config().AnvilTextLimit);
    }


}
