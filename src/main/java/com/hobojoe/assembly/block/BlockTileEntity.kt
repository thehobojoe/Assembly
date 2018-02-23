package com.hobojoe.assembly.block

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

abstract class BlockTileEntity<TE : TileEntity>(material: Material, name: String) :
    BlockBase(material, name) {

    abstract fun getTileEntityClass() : Class<TE>

    fun getTileEntity(world: World, pos: BlockPos) : TE {
        return world.getTileEntity(pos) as TE
    }

    override fun hasTileEntity(state: IBlockState?): Boolean {
        return true
    }

    abstract override fun createTileEntity(world: World?, state: IBlockState?): TileEntity?

}