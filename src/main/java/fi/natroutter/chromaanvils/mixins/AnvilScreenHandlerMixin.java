package fi.natroutter.chromaanvils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.sun.source.doctree.SeeTree;
import fi.natroutter.chromaanvils.ChromaAnvils;
import fi.natroutter.chromaanvils.utilities.Colors;
import fi.natroutter.chromaanvils.utilities.Utils;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    @Shadow private @Nullable String newItemName;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
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
    private void ModifyResult(ItemStack stack) {
        if (MinecraftClient.getInstance().isIntegratedServerRunning()) {
            if (this.newItemName != null && !ChromaAnvils.config().isBlacklisted(stack)) {

                String clamped = this.newItemName.substring(0,Math.min(this.newItemName.length(), ChromaAnvils.config().NameLimit));

                Component comp = Colors.deserialize(clamped);
                String serialize = Colors.serialize(comp);

                String name = Utils.extractWithTags(serialize, ChromaAnvils.config().NameLimit);

                Component finalComp = Colors.deserialize(name);
                stack.set(DataComponentTypes.CUSTOM_NAME, Colors.toNative(finalComp));
            }
        } else {
            String comp = this.newItemName.substring(0,Math.min(this.newItemName.length(), ChromaAnvils.config().NameLimit));
            String name = Utils.extractWithTags(comp, ChromaAnvils.config().NameLimit);
            stack.set(DataComponentTypes.CUSTOM_NAME, Text.of(name));
        }

    }
}