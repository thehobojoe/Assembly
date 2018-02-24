package com.hobojoe.assembly.inventory

import net.minecraft.inventory.Container
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList



class InventoryCraftingMatrix : InventoryCrafting(null, 3, 3) {

    var inventory = NonNullList.withSize(9, ItemStack.EMPTY)
    lateinit var eventHandler: Container


    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     * @param index - the slot to check
     * @param stack - the itemstack to check
     */
    override fun setInventorySlotContents(index: Int, stack: ItemStack) {
        inventory[index] = stack
        if(!stack.isEmpty && stack.count > inventoryStackLimit) {
            stack.count = inventoryStackLimit
        }

        eventHandler.onCraftMatrixChanged(this)
    }

    /**
     * Returns the itemstack in the slot specified (Top left is 0, 0). Args: row, column
     * @param row - the row
     * @param col - the column
     */
    override fun getStackInRowAndColumn(row: Int, col: Int): ItemStack? {
        if (row in 0..2) {
            val k = row + col * 3
            return this.getStackInSlot(k)
        } else {
            return null
        }
    }


    /**
     * Returns the stack in slot i
     * @param slotIndex - the slot to check
     */
    override fun getStackInSlot(slotIndex: Int): ItemStack {
        if (slotIndex >= this.sizeInventory) throw IndexOutOfBoundsException("Tried to get matrix slot out of bounds")
        return this.inventory[slotIndex]
    }

    override fun getName() = "crafting.inventory"
    override fun getInventoryStackLimit() = 1

    override fun getSizeInventory(): Int {
        return 9
    }


    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     * @param slotIndex - the slot to take from
     * @param amount - the amount to decrement
     */
    override fun decrStackSize(slotIndex: Int, amount: Int): ItemStack? {
        if (!this.inventory[slotIndex].isEmpty) {
            val itemstack: ItemStack

            if (this.inventory[slotIndex].count <= amount) {
                itemstack = this.inventory[slotIndex]
                this.inventory[slotIndex] = ItemStack.EMPTY
                this.eventHandler.onCraftMatrixChanged(this)
                return itemstack
            } else {
                itemstack = this.inventory[slotIndex].splitStack(amount)

                if (this.inventory[slotIndex].count == 0) {
                    this.inventory[slotIndex] = ItemStack.EMPTY
                }

                this.eventHandler.onCraftMatrixChanged(this)
                return itemstack
            }
        } else {
            return null
        }
    }



    fun writeToNBT() : NBTTagCompound {
        // Write the ItemStacks in the inventory to NBT
        val tagList = NBTTagList()

        for (i in 0 until inventory.count()) {
            val slot = inventory[i]
            if (!slot.isEmpty) {
                val tagCompound = NBTTagCompound()
                tagCompound.setByte("Slot", i.toByte())
                slot.writeToNBT(tagCompound)
                tagList.appendTag(tagCompound)
            }
        }
        val nbt = NBTTagCompound()
        nbt.setTag("Items", tagList)
        nbt.setInteger("Size", inventory.size)
        return nbt
    }


    fun readFromNBT(nbt: NBTTagCompound?) {
        if(nbt == null) return

        val tagList = nbt.getTagList("Items", 10)
        for (i in 0 until tagList.tagCount()) {
            val tag = tagList.getCompoundTagAt(i)
            val slot = tag.getInteger("Slot")
            if(slot >= 0 && slot < inventory.size) {
                inventory[slot] = ItemStack(tag)
            }
        }
    }
}