package net.Ryaas.firstmod.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TelekineticAscenderItem extends Item {
    public TelekineticAscenderItem(Properties properties) {
        super(properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) { // Ensure this only runs on the server side
            ItemStack itemStack = player.getItemInHand(hand);

            CompoundTag playerData = player.getPersistentData();

            // Check if the player does not have "AbilitySet" set to 1
            if (!playerData.contains("AbilitySet") || playerData.getInt("AbilitySet") != 1) {
                // Set or update the AbilitySet value to 1
                playerData.putInt("AbilitySet", 1);

                // Provide feedback to the player about setting or updating their ability set
                if (!playerData.contains("AbilitySet")) {
                    // Feedback for setting the ability set for the first time
                    player.sendSystemMessage(Component.literal("Ability Set granted!"));
                } else {
                    // Feedback for updating the ability set to 1
                    player.sendSystemMessage(Component.literal("Ability Set updated to 1."));
                }

                // Optional: Consume the item after use
                itemStack.shrink(1);

                return InteractionResultHolder.success(itemStack);
            } else {
                // Feedback if the player already has "AbilitySet" set to 1
                player.sendSystemMessage(Component.literal("Your Ability Set is already set to 1."));
            }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
