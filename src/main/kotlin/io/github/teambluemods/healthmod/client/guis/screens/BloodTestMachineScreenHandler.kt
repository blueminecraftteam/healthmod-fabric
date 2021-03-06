/*
 * Copyright (c) 2020, 2021 Team Blue.
 *
 * This file is part of HealthMod Fabric.
 *
 * HealthMod Fabric is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod Fabric is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with HealthMod Fabric.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.teambluemods.healthmod.client.guis.screens

import io.github.teambluemods.healthmod.registries.ScreenHandlerTypeRegistries
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot

class BloodTestMachineScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    private val inventory: Inventory
) : ScreenHandler(ScreenHandlerTypeRegistries.BLOOD_TEST_MACHINE, syncId) {
    init {
        checkSize(inventory, 3)

        inventory.onOpen(playerInventory.player)

        // blood test machine inventory
        addSlot(Slot(inventory, 0, 56, 51))
        addSlot(Slot(inventory, 1, 79, 58))
        addSlot(Slot(inventory, 2, 102, 51))

        // player inventory
        for (row in 0 until 3) {
            for (column in 0 until 9) {
                addSlot(Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18))
            }
        }

        // player hotbar
        for (column in 0 until 9) {
            addSlot(Slot(playerInventory, column, 8 + column * 18, 142))
        }
    }

    constructor(syncId: Int, playerInventory: PlayerInventory) : this(syncId, playerInventory, SimpleInventory(6))

    override fun canUse(player: PlayerEntity) = inventory.canPlayerUse(player)

    override fun transferSlot(player: PlayerEntity, invSlot: Int): ItemStack {
        var newStack = ItemStack.EMPTY
        val slot = slots[invSlot] ?: return newStack

        if (slot.hasStack()) {
            val originalStack = slot.stack
            newStack = originalStack.copy()

            if (invSlot < inventory.size()) {
                if (!insertItem(originalStack, inventory.size(), slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!insertItem(originalStack, 0, inventory.size(), false)) {
                return ItemStack.EMPTY
            }

            if (originalStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return newStack
    }
}