package com.hobojoe.assembly.assembler

import com.hobojoe.assembly.AssemblyMod
import com.hobojoe.assembly.block.ModBlocks
import com.hobojoe.assembly.inventory.ContainerAssembler
import com.hobojoe.assembly.inventory.SlotCraftingResult
import com.hobojoe.assembly.inventory.SlotGhost
import com.hobojoe.assembly.render.GuiRender
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation


class GuiAssembler(private val container: ContainerAssembler, private var playerInv: InventoryPlayer) :
    GuiContainer(container) {

    private val guiHeight = 250

    companion object {
        private val BG_TEXTURE = ResourceLocation(AssemblyMod.MODID, "textures/gui/assembler.png")
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawDefaultBackground()
        super.drawScreen(mouseX, mouseY, partialTicks)
    }


    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        GlStateManager.color(1f, 1f, 1f, 1f)
        mc.textureManager.bindTexture(BG_TEXTURE)

        val x = (width - xSize) / 2
        val y = (height - guiHeight) / 2
        drawTexturedModalRect(x, y, 0, 0, xSize, guiHeight)
    }

    override fun drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) {
        val name = I18n.format(ModBlocks.assembler.unlocalizedName + ".name")
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, ((ySize-guiHeight) / 2) + 4, 0x404040)
        fontRenderer.drawString(playerInv.displayName.unformattedText, 8, ((ySize-guiHeight) / 2) + 158, 0x404040)

        val matches = container.getMatches()
        var i = 0
        for (slot in this.container.inventorySlots) {
            when(slot) {
                is SlotGhost -> {
                    var match = true
                    if(matches.size > i) {
                        match = matches[i]
                    }
                    this.drawItemStack(slot.stack, slot.xPos, slot.yPos, match)
                    i++
                }
                is SlotCraftingResult -> {
                    val match = container.hasIngredients()
                    this.drawItemStack(slot.stack, slot.xPos, slot.yPos, match, true)
                }
            }
        }
    }

    private fun drawItemStack(stack: ItemStack, x: Int, y: Int, hasIngredient: Boolean, solid: Boolean = false) {
        val overlay = if(hasIngredient) {
            0xff8a8a8a.toInt()
        } else {
            if(solid) {
                0x80ff0000.toInt()
            } else {
                0xffff0000.toInt()
            }

        }
        val transparent = 0x808a8a8a.toInt()
        if(stack != ItemStack.EMPTY) {
            GuiRender.paintOverlay(x, y, 16, overlay)
            GuiRender.paintItem(stack, x, y, mc, mc.renderItem, 200f)
            if(!solid) {
                GuiRender.paintOverlay(x, y, 16, transparent)
            }

        }
    }
}