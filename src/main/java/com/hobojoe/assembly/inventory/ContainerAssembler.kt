package com.hobojoe.assembly.inventory

import com.hobojoe.assembly.assembler.TileEntityAssembler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryCraftResult
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.world.World
import net.minecraftforge.items.ItemStackHandler
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

    override fun tookResult() : Boolean {
        println("took result")

        val invCopy = List(27, { i ->
            assemblerInv.getStackInSlot(i)
        })
        val affectedStacks = ArrayList<Int>()
        val affectedMatrixStacks = ArrayList<Int>()

        var hasMatch = false
        recipe?.let { recipe ->

            ingred@ for (i in 0 until recipe.size) {
                val ingredient = recipe[i]
                for (stack in ingredient.matchingStacks) {
                    for(j in 0 until invCopy.size) {
                        val invStack = invCopy[j]
                        if (invStack.isItemEqual(stack)) {
                            println("recipe item match: ${stack.unlocalizedName}")
                            hasMatch = true
                            invCopy[j].shrink(1)
                            //check if crafting grid has this
                            swap@ for(i in 0 until craftMatrix.sizeInventory) {
                                if(affectedMatrixStacks.contains(i)) continue
                                val gridStack = craftMatrix.getStackInSlot(i)
                                for(validInput in ingredient.matchingStacks) {
                                    if(gridStack?.isItemEqual(validInput) == true) {
                                        println("found match in grid, altering slot")
                                        craftMatrix.setInventorySlotContents(i, stack)
                                        affectedMatrixStacks.add(i)
                                        break@swap
                                    }
                                }

                            }
                            affectedStacks.add(j)
                            continue@ingred
                        }
                    }
                    hasMatch = false
                }
            }
        }

        println("HAS MATCH: " + hasMatch)
        if(hasMatch) {
            for(index in affectedStacks) {
                assemblerInv.setStackInSlot(index, invCopy[index])
            }
            //onCraftMatrixChanged(craftMatrix)
        }

        return hasMatch
    }

    private var recipe: List<Ingredient>? = null

    override fun onCraftMatrixChanged(inventoryIn: IInventory?) {
        val output = CraftingManager.findMatchingResult(craftMatrix, player.world) ?: ItemStack.EMPTY
        recipe = CraftingManager.findMatchingRecipe(craftMatrix, player.world)?.ingredients


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