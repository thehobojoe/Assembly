package com.hobojoe.assembly

import com.hobojoe.assembly.assembler.GuiAssembler
import com.hobojoe.assembly.assembler.TileEntityAssembler
import com.hobojoe.assembly.inventory.ContainerAssembler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

class GuiHandler : IGuiHandler {


    companion object {
        const val ASSEMBLER = 0
    }

    override fun getClientGuiElement(
        ID: Int,
        player: EntityPlayer?,
        world: World?,
        x: Int,
        y: Int,
        z: Int
    ): Any? {
        return when(ID) {
            ASSEMBLER -> GuiAssembler(getServerGuiElement(ID, player, world, x, y, z) as ContainerAssembler, player!!.inventory)
            else -> null
        }
    }

    override fun getServerGuiElement(
        ID: Int,
        player: EntityPlayer?,
        world: World?,
        x: Int,
        y: Int,
        z: Int
    ): Any? {
        return when(ID) {
            ASSEMBLER -> ContainerAssembler(player!!, world?.getTileEntity(BlockPos(x, y, z)) as TileEntityAssembler)
            else -> null
        }
    }
}