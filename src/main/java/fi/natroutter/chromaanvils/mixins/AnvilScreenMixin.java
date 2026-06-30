package fi.natroutter.chromaanvils.mixins;

import fi.natroutter.chromaanvils.ChromaAnvils;
import fi.natroutter.chromaanvils.utilities.Colors;
import fi.natroutter.chromaanvils.utilities.Utils;
import net.kyori.adventure.text.Component;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
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

    @ModifyConstant(method = "extractLabels", constant = @Constant(intValue = 40))
    private int disableTooExpensiveLabelLimit(int limit) {
        return ChromaAnvils.disableTooExpensiveLimit() ? Integer.MAX_VALUE : limit;
    }


    @Inject(method = "slotChanged", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/EditBox;setValue(Ljava/lang/String;)V",
            shift = At.Shift.AFTER,
            ordinal = 0
    ))
    private void updateResult(AbstractContainerMenu abstractContainerMenu, int i, ItemStack itemStack, CallbackInfo ci) {
        if (this.player instanceof ServerPlayer serverPlayer) {
            boolean hasPerms = Utils.hasPermission(serverPlayer, "use", false);
            if (!hasPerms) return;
        }

        String itemName = itemStack.getItemName().getString();
        Component comp = Colors.toAdventure(itemStack.getCustomName());

        if (comp != null && comp.getClass().getTypeName().endsWith("TextComponentImpl")) {
            itemName = Colors.serialize(comp);
        }
        name.setValue(itemStack.isEmpty() ? "" : itemName);
    }
}
