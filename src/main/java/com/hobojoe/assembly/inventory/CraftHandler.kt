package com.hobojoe.assembly.inventory

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.items.ItemStackHandler

class CraftHandler(var inventory: ItemStackHandler) {

    private var copy = getInventoryClone()
    private var matches: BooleanArray = BooleanArray(0)
    private val affectedStacks = ArrayList<Int>()


    fun craft(recipe: List<Ingredient>) : Boolean {

        calculateMatches(recipe)
        if(hasIngredients()) {
            for(index in affectedStacks) {
                inventory.setStackInSlot(index, copy[index])
            }
            return true
        }
        return false
    }


    fun getMatches() = matches

    fun hasIngredients() : Boolean {
        if(matches.isEmpty()) return false
        for(match in matches) {
            if (!match) return false
        }
        return true
    }

    fun clearMatches() {
        matches = BooleanArray(0) {false}
    }


    fun calculateMatches(recipe: List<Ingredient>) : BooleanArray {
        copy = getInventoryClone()
        matches = BooleanArray(recipe.size) {false}
        // iterate through each ingredient
        for((index, ingredient) in recipe.withIndex()) {
            if(ingredient == Ingredient.EMPTY) {
                println("ingredient is empty, setting true match and continuing")
                matches[index] = true
                continue
            }
            //println("checking ingredient")
            // iterate through inventory to find valid matches
            if(checkIngredientMatchInInventory(ingredient)) {
                println("recipe item match at index $index")
                matches[index] = true
            } else {
                println("no match found at index $index")
            }
        }
        return matches
    }

    private fun checkIngredientMatchInInventory(ingredient: Ingredient) : Boolean {
        // iterate through possible valid replacements for ingredient
        for(i in 0 until copy.size) {
            val stack = copy[i]
            if (stack.isEmpty) continue

            ingredient.matchingStacks.firstOrNull { it.isItemEqual(stack) }?.let {

                stack.shrink(1)
                if (!affectedStacks.contains(i)) {
                    affectedStacks.add(i)
                }
                return true
            }
        }
        return false
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
