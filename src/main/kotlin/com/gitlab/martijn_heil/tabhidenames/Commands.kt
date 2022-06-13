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

import com.gitlab.martijn_heil.nincommands.common.CommandTarget
import com.sk89q.intake.Command
import com.sk89q.intake.Require
import org.bukkit.entity.Player

class Commands {
    @Command(aliases = ["hidename"], desc = "Hide a player's name tag from everyone")
    @Require("tabhidenames.hidename")
    fun hidename(@CommandTarget("tabhidenames.hidename.others") target: Player) {
        TAPI.teamManager.hideNametag(target.TABPlayer!!)
    }

    @Command(aliases = ["showname"], desc = "Show a player's name tag from everyone")
    @Require("tabhidenames.showname")
    fun showname(@CommandTarget("tabhidenames.showname.others") target: Player) {
        TAPI.teamManager.showNametag(target.TABPlayer!!)
    }

    @Command(aliases = ["hidenametags"], desc = "Hide nametags from other players from your yourself")
    @Require("tabhidenames.hidenametags")
    fun hidenametags(@CommandTarget("tabhidenames.hidenametags.others") target: Player) {
        target.TABPlayer!!.hasHideNameTags = true
    }

    @Command(aliases = ["shownametags"], desc = "Unhide nametags from other players from your yourself")
    @Require("tabhidenames.shownametags")
    fun shownametags(@CommandTarget("tabhidenames.shownametags.others") target: Player) {
        target.TABPlayer!!.hasHideNameTags = false
    }
}
