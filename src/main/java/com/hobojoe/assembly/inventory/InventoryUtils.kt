package com.hobojoe.assembly.inventory

import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList

object InventoryUtils {

}

fun NonNullList<ItemStack>.hasMatch(stack: ItemStack) : Boolean {
    return this.firstOrNull { it.isItemEqual(stack) } != null
}

fun NonNullList<ItemStack>.getMatch(stack: ItemStack) : ItemStack? {
    return this.firstOrNull { it.isItemEqual(stack) }
}

fun NonNullList<ItemStack>.getMatchIndex(stack: ItemStack) : Int {
    return this.indexOfFirst { it.isItemEqual(stack) }
}

