package net.Ryaas.firstmod.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AbilityRemoverItem extends Item {

    public AbilityRemoverItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) { // Ensure this only runs on the server side
            ItemStack itemStack = player.getItemInHand(hand);

            CompoundTag playerData = player.getPersistentData();

            // Check if the player already has the "AbilitySet" key
            if (playerData.contains("AbilitySet")) {
                // Remove the AbilitySet key
                playerData.remove("AbilitySet");

                // Optional: Provide feedback to the player (e.g., via chat message)
                player.sendSystemMessage(Component.literal("Ability Set removed!"));

                // Optional: Consume the item after use
                itemStack.shrink(1);

                return InteractionResultHolder.success(itemStack);
            } else {
                // Optional: Provide feedback that the player does not have the ability set
                player.sendSystemMessage(Component.literal("You do not have an Ability Set to remove."));
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}