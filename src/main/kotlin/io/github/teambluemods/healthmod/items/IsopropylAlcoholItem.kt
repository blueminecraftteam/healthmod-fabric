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

package io.github.teambluemods.healthmod.items

import io.github.teambluemods.healthmod.HealthMod
import io.github.teambluemods.healthmod.registries.ComponentRegistries
import io.github.teambluemods.healthmod.util.extensions.minusAssign
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.TranslatableText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class IsopropylAlcoholItem(settings: Settings) : Item(settings) {
    // TODO make fire grow faster if thrown into fire (because isopropyl alcohol is flammable)

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val hasSanitizedWoundComponent = ComponentRegistries.SANITIZED_WOUND.get(user)
        val stackInHand = user.getStackInHand(hand)

        return if (user.health < user.maxHealth) {
            // TODO wounded component
            if (!hasSanitizedWoundComponent.value) {
                hasSanitizedWoundComponent.value = true
                stackInHand -= 1

                user.sendMessage(TranslatableText("text.${HealthMod.MOD_ID}.isopropyl_alcohol.apply"), true)

                user.damage(DamageSource.GENERIC, 0.5F)

                TypedActionResult.consume(stackInHand)
            } else {
                TypedActionResult.pass(stackInHand)
            }
        } else {
            TypedActionResult.pass(stackInHand)
        }
    }
}