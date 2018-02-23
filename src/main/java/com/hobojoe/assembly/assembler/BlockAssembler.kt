package com.hobojoe.assembly.assembler

import com.hobojoe.assembly.AssemblyMod
import com.hobojoe.assembly.GuiHandler
import com.hobojoe.assembly.block.BlockTileEntity
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.CapabilityItemHandler

class BlockAssembler : BlockTileEntity<TileEntityAssembler>(Material.ROCK, "assembler") {

    init {
        unlocalizedName = "${AssemblyMod.MODID}.$name"
        setRegistryName(name)
        setHardness(3f)
        setResistance(5f)
        setHarvestLevel("pickaxe", 2)
        setCreativeTab(CreativeTabs.MISC)
    }

    override fun onBlockActivated(
        world: World?,
        position: BlockPos?,
        state: IBlockState?,
        player: EntityPlayer?,
        hand: EnumHand?,
        facing: EnumFacing?,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean {

        if(world?.isRemote == false) {
            println("attempting to open gui")
            position?.let { pos ->
                player?.openGui(AssemblyMod.instance, GuiHandler.ASSEMBLER, world, pos.x, pos.y, pos.z)
            }
        }
        return true
    }

    override fun breakBlock(world: World?, pos: BlockPos?, state: IBlockState?) {
        val tile: TileEntityAssembler = getTileEntity(world!!, pos!!)
        val itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH)
        val stack = itemHandler?.getStackInSlot(0)
        if(stack?.isEmpty == false) {
            val item = EntityItem(world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
            world.spawnEntity(item)
        }
        super.breakBlock(world, pos, state)
    }

    override fun getTileEntityClass(): Class<TileEntityAssembler> {
        return TileEntityAssembler::class.java
    }

    override fun createTileEntity(world: World?, state: IBlockState?): TileEntity? {
        return TileEntityAssembler()
    }
}