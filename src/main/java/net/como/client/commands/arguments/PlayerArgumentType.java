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

public class PlayerArgumentType implements ArgumentType<PlayerEntity> {
    private static final DynamicCommandExceptionType NO_SUCH_PLAYER = new DynamicCommandExceptionType(name -> Text.literal("Player with name " + name + " doesn't exist."));

    public static PlayerArgumentType create() {
        return new PlayerArgumentType();
    }

    public PlayerArgumentType() {}

    public static PlayerEntity get(CommandContext<?> context, String name) {
        return context.getArgument(name, PlayerEntity.class);
    }

    @Override
    public PlayerEntity parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.readString();
        PlayerEntity playerEntity = null;

        for (PlayerEntity p : ComoClient.getInstance().getClient().world.getPlayers()) {
            if (!p.getName().getString().equalsIgnoreCase(argument)) {
                continue;
            }

            playerEntity = p;
            break;
        }
        if (playerEntity == null) throw NO_SUCH_PLAYER.create(argument);
        return playerEntity;
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