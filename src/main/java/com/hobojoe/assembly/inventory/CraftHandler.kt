package com.hobojoe.assembly.inventory

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.items.ItemStackHandler

class CraftHandler(private val recipe: List<Ingredient>,
                   private val inventory: ItemStackHandler,
                   private val matrix: InventoryCraftingMatrix) {

    private var copy1 = getInventoryClone()
    private var copy2 = getInventoryClone()
    private val affectedStacks = ArrayList<Int>()
    private val affectedMatrixStacks = ArrayList<Int>()


    fun craft() : Boolean {

        println("checking for exact ingredients")
        if(hasExactIngredients()) {
            println("has exact ingredients")
            for(index in affectedStacks) {
                inventory.setStackInSlot(index, copy1[index])
            }
            return true
        }

        println("checking for inexact ingredients")
        if(hasInexactIngredients()) {
            println("has inexact ingredients")
            for(index in affectedStacks) {
                inventory.setStackInSlot(index, copy2[index])
            }
            return true
        }
        println("does not have inexact ingredients")

        return false
    }


    private fun hasExactIngredients() : Boolean {

        for (stack in matrix.inventory) {
            if(stack.isEmpty) continue
            val index = copy1.indexOfFirst { stack.isItemEqual(it) }
            if(index < 0) {
                // no match, reset matched slots and return
                println("does NOT contain exact ingredients")
                affectedStacks.clear()
                return false
            }

            println("found match for ${copy1[index].unlocalizedName}")
            copy1[index].shrink(1)
            if(!affectedStacks.contains(index)) {
                affectedStacks.add(index)
            }
        }
        return true
    }


    private fun hasInexactIngredients() : Boolean {

        var hasMatch = false
        // iterate through each ingredient
        recipe.forEach recipe@ { ingredient ->
            //println("checking ingredient")
            // iterate through inventory to find valid matches
            for(i in 0 until copy2.size) {
                val stack = copy2[i]
                if(stack.isEmpty) continue // save some iteration steps if stack is empty

                // iterate through possible valid replacements for ingredient
                ingredient.matchingStacks.firstOrNull { it.isItemEqual(stack) }?.let {

                    println("recipe item match: ${stack.displayName}")
                    hasMatch = true
                    val newItem = stack.copy()
                    println("editing stack of ${stack.displayName} with count ${stack.count}")
                    stack.shrink(1)
                    if(!affectedStacks.contains(i)) {
                        affectedStacks.add(i)
                    }

                    // if there is a match, replace item in grid with alternative
                    matrix.inventory.forEachIndexed grid@ { j, gridStack ->
                        // don't replace an item that's already been replaced
                        if(affectedMatrixStacks.contains(j)) return@grid

                        ingredient.matchingStacks.firstOrNull { it.isItemEqual(gridStack) }?.let {

                            println("new item is ${newItem.displayName}")
                            matrix.setInventorySlotContents(j, newItem)
                            affectedMatrixStacks.add(j)
                            return@recipe
                        }
                        gridStack.shrink(1)
                    }
                }
            }
        }
        return hasMatch
    }

    private fun getInventoryClone() : ArrayList<ItemStack> {
        val inv = ArrayList<ItemStack>()
        for(i in 0 until inventory.slots) {
            val stack = inventory.getStackInSlot(i)
            val item = ItemStack(stack.item, stack.count, stack.metadata, stack.serializeNBT())
            inv.add(item)
        }
        return inv
    }
}
