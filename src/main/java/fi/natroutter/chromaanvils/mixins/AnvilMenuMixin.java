package fi.natroutter.chromaanvils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import fi.natroutter.chromaanvils.ChromaAnvils;
import fi.natroutter.chromaanvils.utilities.Colors;
import fi.natroutter.chromaanvils.utilities.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {


    @Shadow
    @Final
    @Mutable
    public static int MAX_NAME_LENGTH;

    @Shadow private @Nullable String itemName;

    @Shadow
    @Final
    private DataSlot cost;

    @Shadow
    private boolean onlyRenaming;

    @Unique
    private Player chromaAnvils$player;

    @Unique
    private int chromaAnvils$uncappedCost;


    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    private void modifyConstructor(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        this.chromaAnvils$player = inventory.player;
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

    @Inject(method = "createResult", at = @At("HEAD"))
    private void resetUncappedCost(CallbackInfo ci) {
        this.chromaAnvils$uncappedCost = 0;
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void enforceTooExpensiveResultLimit(CallbackInfo ci) {
        if (isTooExpensiveBlocked()) {
            AnvilMenu menu = (AnvilMenu) (Object) this;
            this.cost.set(40);
            menu.getSlot(AnvilMenu.RESULT_SLOT).set(ItemStack.EMPTY);
            menu.broadcastChanges();
        }
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

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40, ordinal = 2))
    private int disableTooExpensiveResultLimit(int limit) {
        return ChromaAnvils.disableTooExpensiveLimit() ? Integer.MAX_VALUE : limit;
    }

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40, ordinal = 1))
    private int disableRenameOnlyTooExpensiveCostCap(int limit) {
        return ChromaAnvils.disableTooExpensiveLimit() ? Integer.MAX_VALUE : limit;
    }

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 39))
    private int keepRenameOnlyCostsTooExpensiveWhenLimitIsEnabled(int cap) {
        return ChromaAnvils.disableTooExpensiveLimit() ? cap : 40;
    }

    @ModifyArg(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V",
                    ordinal = 5
            ),
            index = 0
    )
    private int captureUncappedCost(int cost) {
        this.chromaAnvils$uncappedCost = cost;
        return cost;
    }

    @ModifyArg(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V",
                    ordinal = 6
            ),
            index = 0
    )
    private int applyRenameOnlyCostCap(int cap) {
        return ChromaAnvils.disableTooExpensiveLimit() ? this.chromaAnvils$uncappedCost : cap;
    }

    @ModifyArg(
            method = "createResult",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 4
            ),
            index = 1
    )
    private ItemStack hideTooExpensiveResult(ItemStack stack) {
        return isTooExpensiveBlocked() ? ItemStack.EMPTY : stack;
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void enforceTooExpensivePickupLimit(Player player, boolean hasStack, CallbackInfoReturnable<Boolean> cir) {
        if (isTooExpensiveBlocked(player)) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean isTooExpensiveBlocked() {
        return isTooExpensiveBlocked(this.chromaAnvils$player);
    }

    @Unique
    private boolean isTooExpensiveBlocked(@Nullable Player player) {
        if (ChromaAnvils.disableTooExpensiveLimit() || player == null || player.hasInfiniteMaterials()) {
            return false;
        }
        if (this.cost.get() >= 40) {
            return true;
        }
        return this.chromaAnvils$uncappedCost >= 40 || (this.onlyRenaming && this.cost.get() >= 39);
    }

    @Unique
    private void ModifyResult(ItemStack stack) {
        if (this.itemName == null) return;
        if (ChromaAnvils.config().isBlacklisted(stack)) return;

        Player player = this.chromaAnvils$player;

        //Handle anvils on integrated singleplayer server.
        if (player instanceof ServerPlayer && !ChromaAnvils.isDedicatedServer()) {
            applyFormattedName(stack);
            return;
        }


        //Handle anvils on multiplayer servers
        if (player instanceof ServerPlayer serverPlayer) {
            if (!Utils.hasPermission(serverPlayer, "use", false)) return;

            if (Utils.shouldUsePermissions()) {
                applyFormattedName(stack, Utils.GetTagsFromPlayerPermissions(serverPlayer));
            } else {
                applyFormattedName(stack);
            }
            return;
        }

        applyFormattedName(stack);
    }

    @Unique
    private void applyFormattedName(ItemStack stack) {
        String clamped = this.itemName.substring(0,Math.min(this.itemName.length(), ChromaAnvils.config().NameLimit));

        Component comp = Colors.deserialize(clamped);
        String serialize = Colors.serialize(comp);

        String name = Utils.extractWithTags(serialize, ChromaAnvils.config().NameLimit);

        Component finalComp = Colors.deserialize(name);
        stack.set(DataComponents.CUSTOM_NAME, Colors.toNative(finalComp));
    }

    @Unique
    private void applyFormattedName(ItemStack stack, TagResolver[] tags) {
        String clamped = this.itemName.substring(0,Math.min(this.itemName.length(), ChromaAnvils.config().NameLimit));

        Component comp = Colors.deserialize(clamped, tags);
        String serialize = Colors.serialize(comp);

        String name = Utils.extractWithTags(serialize, ChromaAnvils.config().NameLimit);

        Component finalComp = Colors.deserialize(name, tags);
        stack.set(DataComponents.CUSTOM_NAME, Colors.toNative(finalComp));
    }
}
