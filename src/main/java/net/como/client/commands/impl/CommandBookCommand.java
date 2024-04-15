package net.como.client.commands.impl;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.como.client.ComoClient;
import net.como.client.commands.Command;
import net.como.client.utils.NbtUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CommandBookCommand extends Command {
    private final int PADDING_CHARS = 553;

    public CommandBookCommand() {
        super("commandbook", "Gives you a book to run a command", "cmdbook");
    }

    private ItemStack createItem(String command) throws CommandSyntaxException {
        String name = ComoClient.getInstance().me().getName().getString();

        // Make sure that the command starts with /execute run
        if (!command.startsWith("/execute run")) {
            command = "/execute run " + command;
        }

        // Escape the string using GSON
        String escapedCommand;
        escapedCommand = new Gson().toJson(command);
        // It will

        String nbt = "{author:\""+name+"\",pages:['{\"clickEvent\":{\"action\":\"run_command\",\"value\":" + escapedCommand + "},\"text\":\"Thanks for everything."+" ".repeat(PADDING_CHARS)+"\"}','{\"text\":\"\"}','{\"text\":\"\"}'],resolved:1b,title:\"My Tribute\"}";

        // Create the NBT
        NbtCompound tag = NbtUtils.parseNbt(nbt);

        // Create the item
        ItemStack item = new ItemStack(Items.WRITTEN_BOOK);
        item.setNbt(tag);

        return item;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("command", StringArgumentType.greedyString()).executes(context -> {
            // Check that the player is in creative mode
            if (!ComoClient.getInstance().me().isCreative()) throw CREATIVE_EXCEPTION.create();

            // Get the command
            String command = StringArgumentType.getString(context, "command");

            // Create the ItemStack
            ItemStack item = createItem(command);

            // Give the player the item
            NbtUtils.giveItem(item, false);

            return 1;
        }));
    }
}
