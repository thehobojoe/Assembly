package com.hobojoe.assembly.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.SlotCrafting
import net.minecraft.item.ItemStack

class SlotCraftingResult(
    player: EntityPlayer,
    craftingMatrix: InventoryCraftingMatrix,
    var result: IInventory,
    slotIndex: Int,
    xPos: Int,
    yPos: Int
) : SlotCrafting(player, craftingMatrix, result, slotIndex, xPos, yPos) {

    interface OnCraft {
        fun tookResult() : Boolean
    }

    var craftListener: OnCraft? = null

    override fun onTake(thePlayer: EntityPlayer?, stack: ItemStack?): ItemStack? {

        if(craftListener?.tookResult() == true) {
            val output = stack?.copy()
            output?.count = 1
            //stack?.grow(1)
            result.setInventorySlotContents(0, output)
        } else {
            super.onTake(thePlayer, stack)
        }

        return stack
    }
}