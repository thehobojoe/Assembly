package com.hobojoe.assembly.inventory

import com.hobojoe.assembly.assembler.TileEntityAssembler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryCraftResult
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.world.World
import net.minecraftforge.items.ItemStackHandler
import net.minecraftforge.items.SlotItemHandler

class ContainerAssembler(private val player: EntityPlayer, val assembler: TileEntityAssembler) : Container() {

    private val craftMatrix = assembler.craftMatrix
    private val result = assembler.result
    val assemblerInv = assembler.inventory

    init {
        craftMatrix.eventHandler = this

        val yOffset = 84 / 2
        var yBase: Int

        // crafting grid result 80x75
        yBase = 75 - yOffset
        addSlotToContainer(SlotCraftingResult(player, craftMatrix, result, 0, 80, yBase))

        // crafting grid 62x13
        for(i in 0 until 3) {
            for (j in 0 until 3) {
                yBase = 13 - yOffset
                addSlotToContainer(Slot(craftMatrix, j + i * 3, 62 + j * 18, yBase + i * 18))
            }
        }

        // assembly inventory 8x102
        for(i in 0 until 3) {
            for(j in 0 until 9) {
                yBase = 102 - yOffset
                //println("adding container slot at: row: $i column: $j")
                addSlotToContainer(object: SlotItemHandler(assemblerInv, j + i * 9, 8 + j * 18, yBase + i * 18) {
                    override fun onSlotChanged() {
                        assembler.markDirty()
                    }
                })
            }
        }

        //player inventory 8x168
        for(i in 0 until 3) {
            for(j in 0 until 9) {
                yBase = 168 - yOffset
                //println("adding player slot at: row: $i column: $j")
                addSlotToContainer(Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, yBase + i * 18))
            }
        }

        // player hotbar 8x226
        for(c in 0 until 9) {
            yBase = 226 - yOffset
            //println("adding hotbar slot at index: $c")
            addSlotToContainer(Slot(player.inventory, c, 8 + c * 18, yBase))
        }
    }


    override fun onCraftMatrixChanged(inventoryIn: IInventory?) {
        val output = CraftingManager.findMatchingRecipe(craftMatrix, player.world)?.recipeOutput ?: ItemStack.EMPTY
        result.setInventorySlotContents(0, output)
    }

    override fun canInteractWith(playerIn: EntityPlayer?) = true

    override fun transferStackInSlot(playerIn: EntityPlayer?, index: Int): ItemStack {
        var stack = ItemStack.EMPTY
        val slot = inventorySlots[index]

        if(slot != null && slot.hasStack) {
            val slotStack = slot.stack
            stack = slotStack.copy()
            val containerSize = inventorySlots.size - (playerIn?.inventory?.mainInventory?.size ?: 0)

            if(index < containerSize) {
                if(!this.mergeItemStack(slotStack, containerSize, inventorySlots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.mergeItemStack(slotStack, 0, containerSize, false)) {
                return ItemStack.EMPTY
            }

            if (slotStack.count == 0) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.onSlotChanged()
            }

            if(slotStack.count == stack.count) {
                return ItemStack.EMPTY
            }

            slot.onTake(playerIn, slotStack)
        }

        return stack
    }
}