package com.hobojoe.assembly.block

import com.hobojoe.assembly.AssemblyMod
import com.hobojoe.assembly.assembler.BlockAssembler
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object ModBlocks {

    private const val id = AssemblyMod.MODID

    @GameRegistry.ObjectHolder("$id:assembler")
    lateinit var assembler: BlockAssembler

    @SideOnly(Side.CLIENT)
    fun initModels() {
        assembler.initModel()
        GameRegistry.registerTileEntity(assembler.getTileEntityClass(), assembler.registryName.toString())
    }
}