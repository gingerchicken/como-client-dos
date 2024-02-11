package net.como.client.commands.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.como.client.ComoClient;
import net.como.client.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class CorruptCommand extends Command {
    public CorruptCommand() {
        super("corrupt", "Corrupts the world");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("charge", IntegerArgumentType.integer(1)).executes(context -> {
            // Check if we are in creative
            if (!ComoClient.getInstance().me().isCreative()) {
                throw CREATIVE_EXCEPTION.create();
            }

            int charge = IntegerArgumentType.getInteger(context, "charge");
            ClientPlayerEntity player = ComoClient.getInstance().me();

            // Get the player's position
            BlockPos below = player.getBlockPos().add(0, -1, 0);
            BlockPos belowSide = below.add(0, 0, -1);

            // Get the held item
            ItemStack heldItem = player.getMainHandStack();
            ItemStack sculkBlock = new ItemStack(Items.SCULK_CATALYST);

            // Create the NBT
            String chargeTag = "{charge:1000,decay_delay:1,facings:[\"east\"],pos:[I;" + below.getX() + "," + below.getY() + "," + below.getZ() + "],update_delay:1}";

            // Set the NBT
            sculkBlock.setNbt(StringNbtReader.parse("{BlockEntityTag:{bloom:1,cursors:[" + chargeTag + ("," + chargeTag).repeat(charge - 1) + "],id:\"minecraft:sculk_skulkBlockalyst\"}}"));

            // Create the block hit results
            BlockHitResult belowBhr = new BlockHitResult(below.toCenterPos(), Direction.DOWN, below, false);
            BlockHitResult belowSideBhr = new BlockHitResult(belowSide.toCenterPos(), Direction.DOWN, belowSide, false);

            MinecraftClient client = MinecraftClient.getInstance();
            client.interactionManager.clickCreativeStack(new ItemStack(Items.SCULK), 36 + player.getInventory().selectedSlot);
            client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, belowSide, Direction.DOWN, 0));

            client.interactionManager.interactBlock(player, Hand.MAIN_HAND, belowSideBhr);
            client.interactionManager.clickCreativeStack(sculkBlock, 36 + player.getInventory().selectedSlot);

            client.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, below, Direction.DOWN, 0));
            client.interactionManager.interactBlock(player, Hand.MAIN_HAND, belowBhr);

            client.interactionManager.clickCreativeStack(heldItem, 36 + player.getInventory().selectedSlot);

            return SINGLE_SUCCESS;
        }));
    }
}
