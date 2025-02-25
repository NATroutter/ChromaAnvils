package fi.natroutter.chromaanvils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import fi.natroutter.chromaanvils.ChromaAnvils;
import fi.natroutter.chromaanvils.utilities.Colors;
import fi.natroutter.chromaanvils.utilities.Utils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.kyori.adventure.text.Component;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.minecraft.text.Text;

import java.util.ArrayList;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerServerMixin extends ForgingScreenHandler {

    @Shadow private @Nullable String newItemName;

    public AnvilScreenHandlerServerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }


    @Inject(method = "updateResult", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;",
            shift = At.Shift.AFTER,
            ordinal = 0
    ))
    private void updateResult(CallbackInfo ci, @Local(ordinal = 1) ItemStack stack) {
        ModifyResult(stack);
    }

    @Inject(method = "setNewItemName", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;",
            shift = At.Shift.AFTER,
            ordinal = 0
    ))
    private void setNewItemName(String newItemName, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) ItemStack stack) {
        ModifyResult(stack);
    }

    @Inject(method = "sanitize", at = @At("HEAD"), cancellable = true)
    private static void sanitize(String name, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(name);
    }

    @Unique
    private TagResolver[] GetTagsFromPlayerPermissions(ServerPlayerEntity player) {
        boolean hasColorPerms = Permissions.check(player, ChromaAnvils.MOD_ID + ".colors", false);
        boolean hasDecorPerms = Permissions.check(player, ChromaAnvils.MOD_ID + ".decorations", false);
        boolean hasFontPerms = Permissions.check(player, ChromaAnvils.MOD_ID + ".font", false);
        boolean hasGradientPerms = Permissions.check(player, ChromaAnvils.MOD_ID + ".gradient", false);
        boolean hasRainbowPerms = Permissions.check(player, ChromaAnvils.MOD_ID + ".rainbow", false);
        boolean hasTransitionPerms = Permissions.check(player, ChromaAnvils.MOD_ID + ".transition", false);
        boolean hasResetPerms = Permissions.check(player, ChromaAnvils.MOD_ID + ".reset", true);
        
        ArrayList<TagResolver> tags = new ArrayList<TagResolver>();
        if (hasColorPerms) tags.add(StandardTags.color());
        if (hasDecorPerms) tags.add(StandardTags.decorations());
        if (hasFontPerms) tags.add(StandardTags.font());
        if (hasGradientPerms) tags.add(StandardTags.gradient());
        if (hasRainbowPerms) tags.add(StandardTags.rainbow());
        if (hasTransitionPerms) tags.add(StandardTags.transition());
        if (hasResetPerms) tags.add(StandardTags.reset());
        int len = tags.size();

        return tags.toArray(new TagResolver[len]);
    }

    @Unique
    private void ModifyResult(ItemStack stack) {
        if (this.newItemName == null) return;

        Text result;

        if (this.player instanceof ServerPlayerEntity serverPlayer) {
            boolean hasPerms = Permissions.check(serverPlayer, ChromaAnvils.MOD_ID + ".use", false);

            if (hasPerms) {
                if (this.newItemName != null && !ChromaAnvils.config().isBlacklisted(stack)) {
                    String clamped = this.newItemName.substring(0,Math.min(this.newItemName.length(), ChromaAnvils.config().NameLimit));

                    TagResolver[] tags = GetTagsFromPlayerPermissions(serverPlayer);

                    Component comp = Colors.deserialize(clamped, tags);
                    String serialize = Colors.serialize(comp);

                    String name = Utils.extractWithTags(serialize, ChromaAnvils.config().NameLimit);

                    Component finalComp = Colors.deserialize(name, tags);

                    stack.set(DataComponentTypes.CUSTOM_NAME, Colors.toNative(finalComp));

                }
            } else {
                String comp = this.newItemName.substring(0,Math.min(this.newItemName.length(), ChromaAnvils.config().NameLimit));
                String name = Utils.extractWithTags(comp, ChromaAnvils.config().NameLimit);
                stack.set(DataComponentTypes.CUSTOM_NAME, Text.of(name));
            }
        }

    }
}