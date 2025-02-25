package fi.natroutter.chromaanvils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import fi.natroutter.chromaanvils.ChromaAnvils;
import fi.natroutter.chromaanvils.utilities.Colors;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyori.adventure.text.Component;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.Console;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    public AnvilScreenMixin(AnvilScreenHandler handler,PlayerInventory playerInventory,Text title,Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Shadow
    private TextFieldWidget nameField;

    @Shadow @Final private PlayerEntity player;

    @Inject(method = "setup", at = @At(value = "TAIL"))
    public void setup(CallbackInfo ci) {
        nameField.setMaxLength(ChromaAnvils.config().AnvilTextLimit);
    }

    @Inject(method = "onSlotUpdate", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setText(Ljava/lang/String;)V",
            shift = At.Shift.AFTER,
            ordinal = 0
    ))
    private void updateResult(CallbackInfo ci, @Local(argsOnly = true) ItemStack stack) {
        if (this.player instanceof ServerPlayerEntity serverPlayer) {
            boolean hasPerms = Permissions.check(serverPlayer, ChromaAnvils.MOD_ID + ".use", false);
            if (!hasPerms) return;
        }

        Text text = stack.getName();
        String name = text.getLiteralString();
        Component comp = Colors.toAdventure(text);

        if (comp != null && comp.getClass().getTypeName().endsWith("TextComponentImpl")) {
            name = Colors.serialize(comp);
        }
        nameField.setText(stack.isEmpty() ? "" : name);
    }

}