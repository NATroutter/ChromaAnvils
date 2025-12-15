package fi.natroutter.chromaanvils.mixins;

import fi.natroutter.chromaanvils.ChromaAnvils;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {


    @Shadow
    @Final
    @Mutable
    public static int MAX_NAME_LENGTH;

//    @Unique
//    private static final ThreadLocal<Inventory> chromaanvils$currentInventory = new ThreadLocal<>();


    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private static void modifyConstructor(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
//        chromaanvils$currentInventory.set(inventory);
        MAX_NAME_LENGTH = ChromaAnvils.config().AnvilTextLimit;
    }

    @Inject(method = "validateName", at = @At("RETURN"), cancellable = true)
    private static void modifyValidation(String string, CallbackInfoReturnable<String> cir) {
        String string2 = StringUtil.filterText(string);
        if (string2.length() <= ChromaAnvils.config().AnvilTextLimit) {
            cir.setReturnValue(string2);
        } else {
            cir.setReturnValue(null);
        }
    }



}
