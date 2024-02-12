package net.como.client.mixin;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import net.como.client.ComoClient;
import net.como.client.commands.Commands;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(ChatInputSuggestor.class)
public abstract class ChatInputSuggestionMixin {
    @Shadow
    private ParseResults<CommandSource> parse;
    @Shadow @Final
    TextFieldWidget textField;
    @Shadow boolean completingSuggestions;
    @Shadow CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow ChatInputSuggestor.SuggestionWindow window;

    @Shadow
    protected abstract void showCommandSuggestions();

    @Inject(method = "refresh", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false), cancellable = true,  locals = LocalCapture.CAPTURE_FAILHARD)
    public void onRefresh(CallbackInfo ci, String string, StringReader reader) {
        // Get the commands
        Commands commands = ComoClient.getInstance().getCommands();

        // Get the prefix
        String prefix = commands.getPrefix();

        // Get the length of the prefix
        int length = prefix.length();

        if (!reader.canRead(prefix.length()) || !reader.getString().startsWith(prefix, reader.getCursor())) return;

        reader.setCursor(reader.getCursor() + length);

        // Ensure that the parse is not null
        if (this.parse == null) {
            this.parse = commands.getDispatcher().parse(reader, commands.getSource());
        }

        // Get the cursor position
        int cursor = textField.getCursor();

        // Make sure that the cursor is not less than 1
        if (cursor < 1) return;

        // Check if the window is not null and if we are completing suggestions
        if (this.window != null && this.completingSuggestions) {
            return;
        }

        // Get the pending suggestions
        this.pendingSuggestions = commands.getDispatcher().getCompletionSuggestions(this.parse, cursor);

        // Run the pending suggestions
        this.pendingSuggestions.thenRun(() -> {
            if (this.pendingSuggestions.isDone()) {
                this.showCommandSuggestions();
            }
        });

        // Cancel the callback (Hijack the method)
        ci.cancel();
    }
}
