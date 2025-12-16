package fi.natroutter.chromaanvils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import fi.natroutter.chromaanvils.ChromaAnvils;
import fi.natroutter.chromaanvils.utilities.Colors;
import fi.natroutter.chromaanvils.utilities.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
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

    @Shadow private @Nullable String itemName;


    @Unique
    private static final ThreadLocal<Inventory> currentInventory = new ThreadLocal<>();

    @Unique
    private static final ThreadLocal<Player> currentPlayer = new ThreadLocal<>();


    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private static void modifyConstructor(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        currentInventory.set(inventory);
        currentPlayer.set(inventory.player);
        MAX_NAME_LENGTH = ChromaAnvils.config().AnvilTextLimit;
    }

    @Inject(method = "setItemName", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;",
            shift = At.Shift.AFTER,
            ordinal = 0
    ))
    private void onSetItemName(String name, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) ItemStack stack) {
        ModifyResult(stack);
    }


    @Inject(method = "createResult", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;",
            shift = At.Shift.AFTER,
            ordinal = 0
        )
    )
    private void updateResult(CallbackInfo ci, @Local(ordinal = 1) ItemStack stack) {
        ModifyResult(stack);
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




    @Unique
    private void ModifyResult(ItemStack stack) {
        if (this.itemName == null) return;

        Player player = currentPlayer.get();


        if (player instanceof ServerPlayer serverPlayer) {
            boolean hasPerms = Utils.hasPermission(serverPlayer, "use", false);

            if (hasPerms) {
                if (this.itemName != null && !ChromaAnvils.config().isBlacklisted(stack)) {
                    String clamped = this.itemName.substring(0,Math.min(this.itemName.length(), ChromaAnvils.config().NameLimit));

                    TagResolver[] tags = Utils.GetTagsFromPlayerPermissions(serverPlayer);

                    Component comp = Colors.deserialize(clamped, tags);
                    String serialize = Colors.serialize(comp);

                    String name = Utils.extractWithTags(serialize, ChromaAnvils.config().NameLimit);

                    Component finalComp = Colors.deserialize(name, tags);

                    stack.set(DataComponents.CUSTOM_NAME, Colors.toNative(finalComp));

                }
            } else {
                String comp = this.itemName.substring(0,Math.min(this.itemName.length(), ChromaAnvils.config().NameLimit));
                String name = Utils.extractWithTags(comp, ChromaAnvils.config().NameLimit);
                stack.set(DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal(name));
            }
        }

    }
}
