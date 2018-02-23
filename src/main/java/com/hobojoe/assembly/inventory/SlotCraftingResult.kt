package com.hobojoe.assembly.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.SlotCrafting
import net.minecraft.item.ItemStack

class SlotCraftingResult(
    player: EntityPlayer,
    craftingMatrix: InventoryCraftingMatrix,
    result: IInventory,
    slotIndex: Int,
    xPos: Int,
    yPos: Int
) : SlotCrafting(player, craftingMatrix, result, slotIndex, xPos, yPos) {

    override fun onTake(thePlayer: EntityPlayer?, stack: ItemStack?): ItemStack? {
        super.onTake(thePlayer, stack)
        return stack
    }
}