package com.hobojoe.assembly.plugins

import mezz.jei.api.IModPlugin
import mezz.jei.api.IModRegistry

@mezz.jei.api.JEIPlugin
class JEIPlugin : IModPlugin {

    override fun register(registry: IModRegistry?) {
        val jeiHelpers = registry?.jeiHelpers

        registry?.recipeTransferRegistry?.addRecipeTransferHandler(AssemblerRecipeTransferInfo())

        //registry?.addRecipeClickArea()
    }
}