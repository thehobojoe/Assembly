package com.hobojoe.assembly.inventory

import com.hobojoe.assembly.assembler.TileEntityAssembler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ClickType
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.items.SlotItemHandler


class ContainerAssembler(private val player: EntityPlayer, val assembler: TileEntityAssembler)
    : Container(), SlotCraftingResult.OnCraft {


    private val craftMatrix = assembler.craftMatrix
    private val result = assembler.result
    val assemblerInv = assembler.inventory

    init {
        craftMatrix.eventHandler = this

        val yOffset = 84 / 2
        var yBase: Int

        // crafting grid result 80x75
        yBase = 75 - yOffset
        val slotResult = SlotCraftingResult(player, craftMatrix, result, 0, 80, yBase)
        slotResult.craftListener = this
        addSlotToContainer(slotResult)

        // crafting grid 62x13
        println("crafting grid indices: ")
        for(i in 0 until 3) {
            for (j in 0 until 3) {
                yBase = 13 - yOffset
                val index = j + i * 3
                print(index.toString() + ", ")
                addSlotToContainer(SlotGhost(craftMatrix, index, 62 + j * 18, yBase + i * 18))
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

    override fun tookResult() : Boolean {

        recipe?.let {
            val handler = CraftHandler(it, assemblerInv, craftMatrix)
            return handler.craft()
        }
        return false
    }

    private var recipe: List<Ingredient>? = null


    override fun onCraftMatrixChanged(inventoryIn: IInventory?) {
        val output = CraftingManager.findMatchingResult(craftMatrix, player.world) ?: ItemStack.EMPTY
        recipe = CraftingManager.findMatchingRecipe(craftMatrix, player.world)?.ingredients

        result.setInventorySlotContents(0, output)
    }

    override fun canInteractWith(playerIn: EntityPlayer) = true

    // For ghost item handling
    override fun slotClick(slotId: Int, dragType: Int, clickTypeIn: ClickType, player: EntityPlayer): ItemStack {

        val slot = if (slotId < 0) null else this.inventorySlots[slotId]
        if (slot is SlotGhost) {
            if (dragType == 2) {
                slot.putStack(ItemStack.EMPTY)
            } else {
                slot.putStack(if (player.inventory.itemStack.isEmpty) ItemStack.EMPTY else player.inventory.itemStack.copy())
            }
            return player.inventory.itemStack
        }

        return super.slotClick(slotId, dragType, clickTypeIn, player)
    }

    override fun transferStackInSlot(playerIn: EntityPlayer, index: Int): ItemStack {
        var stack = ItemStack.EMPTY
        val slot = inventorySlots[index]

        if(slot.hasStack) {

            val slotStack = slot.stack
            stack = slotStack.copy()
            val containerSize = inventorySlots.size - (playerIn.inventory.mainInventory.size)

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