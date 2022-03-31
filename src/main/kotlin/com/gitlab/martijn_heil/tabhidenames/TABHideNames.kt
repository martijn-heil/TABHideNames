/*
 * TABHideNames
 * Copyright (C) 2022  Martijn Heil
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.gitlab.martijn_heil.tabhidenames

import com.gitlab.martijn_heil.nincommands.common.CommonModule
import com.gitlab.martijn_heil.nincommands.common.bukkit.BukkitAuthorizer
import com.gitlab.martijn_heil.nincommands.common.bukkit.provider.BukkitModule
import com.gitlab.martijn_heil.nincommands.common.bukkit.provider.sender.BukkitSenderModule
import com.gitlab.martijn_heil.nincommands.common.bukkit.registerCommand
import com.sk89q.intake.Intake
import com.sk89q.intake.fluent.CommandGraph
import com.sk89q.intake.parametric.ParametricBuilder
import com.sk89q.intake.parametric.provider.PrimitivesModule
import me.neznamy.tab.api.TabAPI
import me.neznamy.tab.api.TabPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

val playersWithNameTagsHidden = HashSet<UUID>()

val Player.TABPlayer: TabPlayer
    get() = TAPI.getPlayer(uniqueId)

var TabPlayer.hasHideNameTags
    get() = playersWithNameTagsHidden.contains(uniqueId)
    set(value) {
        if (value) {
            playersWithNameTagsHidden.add(uniqueId)
        } else {
            playersWithNameTagsHidden.remove(uniqueId)
            Bukkit.getServer().onlinePlayers.map { it.TABPlayer }.forEach { observer ->
                Bukkit.getServer().onlinePlayers.map { it.TABPlayer }.forEach {
                    TAPI.teamManager.showNametag(it, observer)
                }
            }
        }
    }


class TABHideNames : JavaPlugin() {
    override fun onEnable() {
        val injector = Intake.createInjector()
        injector.install(PrimitivesModule())
        injector.install(BukkitModule(server))
        injector.install(BukkitSenderModule())
        injector.install(CommonModule())

        val builder = ParametricBuilder(injector)
        builder.authorizer = BukkitAuthorizer()

        val dispatcher = CommandGraph()
                .builder(builder)
                .commands()
                .registerMethods(Commands())
                .graph()
                .dispatcher

        registerCommand(dispatcher, this, dispatcher.aliases.toList())


        server.scheduler.scheduleSyncRepeatingTask(this, {
            server.onlinePlayers
                    .asSequence()
                    .map { it.TABPlayer }
                    .filter { it.hasHideNameTags }
                    .forEach { observer ->
                        server.onlinePlayers
                                .asSequence()
                                .map { it.TABPlayer }
                                .forEach { TAPI.teamManager.hideNametag(it, observer) }
                    }
        }, 0, 1)
    }

    override fun onDisable() {

    }
}

val TAPI
    get() = TabAPI.getInstance()
