/*
 * Copyright (c) 2020 Blue Minecraft Team.
 *
 * This file is part of HealthMod.
 *
 * HealthMod is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HealthMod is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with HealthMod.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.blueminecraftteam.healthmod.items

import io.github.blueminecraftteam.healthmod.registries.ComponentRegistries
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class IsopropylAlcoholItem(settings: Settings) : Item(settings) {
    // TODO make fire grow faster if thrown into fire (because isopropyl alcohol is flammable)

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val hasSanitizedWoundComponent = ComponentRegistries.SANITIZED_WOUND.get(user)
        val stackInHand = user.getStackInHand(hand)

        return if (hasSanitizedWoundComponent.value) {
            hasSanitizedWoundComponent.value = true
            stackInHand.decrement(1)
            // TODO translatable text
            user.sendMessage(
                LiteralText("The bacteria is gone, but you feel a slight pain at the site of bleeding."),
                true
            )
            user.damage(DamageSource.GENERIC, 0.5F)
            TypedActionResult.consume(stackInHand)
        } else {
            TypedActionResult.pass(stackInHand)
        }
    }
}