package com.hobojoe.assembly.inventory

import net.minecraftforge.items.ItemStackHandler

class InventoryHandler(size: Int) : ItemStackHandler(size) {

    var inventoryListener: (() -> (Unit))? = null

    override fun onContentsChanged(slot: Int) {
        super.onContentsChanged(slot)
        inventoryListener?.invoke()
    }
}