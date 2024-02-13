package net.como.client.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.como.client.ComoClient;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UnsafePlayerArgumentType implements ArgumentType<String> {
    // Kinda like a factory method
    public static UnsafePlayerArgumentType create() {
        return new UnsafePlayerArgumentType();
    }

    public UnsafePlayerArgumentType() {

    }

    public static String get(CommandContext<?> context, String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(ComoClient.getInstance().getClient().world.getPlayers().stream().map(abstractClientPlayerEntity -> abstractClientPlayerEntity.getName().getString()), builder);
    }

    @Override
    public Collection<String> getExamples() {
        // Search through all players and get their names
        List<String> players = new ArrayList<>();

        // Collect all players
        for (PlayerEntity player : ComoClient.getInstance().getClient().world.getPlayers()) {
            players.add(player.getDisplayName().getContent().toString());
        }

        return players;
    }
}