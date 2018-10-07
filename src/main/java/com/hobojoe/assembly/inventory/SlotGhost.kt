package com.hobojoe.assembly.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

class SlotGhost(inventory: IInventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {

    public val width = 16
    public val height = 16


    override fun canTakeStack(player: EntityPlayer?) = false
    override fun isItemValid(stack: ItemStack?) = true


    fun isSlotEnabled() = false


    override fun putStack(stack: ItemStack) {
        if (!isItemValid(stack)) {
            return
        }
        if (!stack.isEmpty) {
            stack.count = 1
        }
        inventory.setInventorySlotContents(this.slotIndex, stack)
        onSlotChanged()
    }
}