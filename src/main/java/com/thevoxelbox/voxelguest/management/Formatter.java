/*
 * Copyright (C) 2011 - 2012, psanker and contributors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following 
 * * Redistributions in binary form must reproduce the above copyright notice, this list of 
 *   conditions and the following disclaimer in the documentation and/or other materials 
 *   provided with the distribution.
 * * Neither the name of The VoxelPlugineering Team nor the names of its contributors may be 
 *   used to endorse or promote products derived from this software without specific prior 
 *   written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.thevoxelbox.voxelguest.management;

import com.thevoxelbox.voxelguest.VanishModule;
import com.thevoxelbox.voxelguest.VoxelGuest;
import com.thevoxelbox.voxelguest.modules.ModuleException;
import com.thevoxelbox.voxelguest.modules.ModuleManager;
import com.thevoxelbox.voxelguest.players.GuestPlayer;

public class Formatter{

    /*
     * -------------- FORMAT HANDLES -------------- $n = name of the player
     * $name = long form of $n $g = group (INDEX 0) of the player $group = long
     * form of $g $gc = code for that group (if desired) $nonline = number of
     * people online
     *
     * Would be best to extend from this implementation so you don't have to
     * rewrite the group crap again
     *
     */

    public static String formatMessage(String in, Object... otherArgs)
    {
        return formatMessages(in, otherArgs)[0];
    }

    public static String[] formatMessages(String in, Object... otherArgs)
    {
        String copy = in;
        boolean guestPlayerParcing;
        
        GuestPlayer gp = (GuestPlayer) otherArgs[0];

        guestPlayerParcing = (gp == null) ? false : true;

        if (guestPlayerParcing) {

            if (gp.getGroups() != null && gp.getGroups().length >= 1) {
                String group = gp.getGroups()[0];
                ConfigurationManager config = VoxelGuest.getGroupManager().getGroupConfiguration(group);
                String groupID = config.getString("group-id");

                copy = copy.replace("$group", group);
                if (groupID != null) {
                    copy = copy.replace("$gc", groupID);
                }
                copy = copy.replace("$g", group);
            }

            try {
                VanishModule module = (VanishModule) ModuleManager.getManager().getModule(VanishModule.class);
                int fakequitNum = module.getFakequitSize();
                if (fakequitNum > 0) {
                    copy = copy.replace("$nonline", String.valueOf(VoxelGuest.ONLINE_MEMBERS - fakequitNum));
                } else {
                    copy = copy.replace("$nonline", String.valueOf(VoxelGuest.ONLINE_MEMBERS));
                }
            } catch (ModuleException ex) {
                copy = copy.replace("$nonline", String.valueOf(VoxelGuest.ONLINE_MEMBERS));
            } catch (NullPointerException ex) {
                copy = copy.replace("$nonline", String.valueOf(VoxelGuest.ONLINE_MEMBERS));
            }

            copy = copy.replace("$name", gp.getPlayer().getName());
            copy = copy.replace("$n", gp.getPlayer().getName());
        }

        copy = encodeColors(copy);

        if (copy.contains("\n")) {
            String[] copies = copy.split("\n");
            return copies;
        } else {
            return new String[]{copy};
        }
    }
    
    public static String encodeColors(String input)
    {
        for (FormatColors color : FormatColors.values()) {
            input = input.replace(color.getColorCode(), color.getBukkitColorCode());
        }

        return input;
    }

    public static String stripColors(String input)
    {
        for (FormatColors color : FormatColors.values()) {
            input = input.replace(color.getColorCode(), "");
            input = input.replace(color.getBukkitColorCode(), "");
        }
        
        return input;
    }
    
    enum FormatColors {

        WHITE("&f"),
        DARK_BLUE("&1"),
        DARK_GREEN("&2"),
        TEAL("&3"),
        DARK_RED("&4"),
        PURPLE("&5"),
        ORANGE("&6"),
        LIGHT_GREY("&7"),
        DARK_GREY("&8"),
        INDIGO("&9"),
        LIGHT_GREEN("&a"),
        CYAN("&b"),
        RED("&c"),
        PINK("&d"),
        YELLOW("&e"),
        BLACK("&0"),
        RANDOMCHAR("&k"),
        BOLD("&l"),
        STRIKETHROUGH("&m"),
        UNDERLINE("&n"),
        ITALIC("&o"),
        RESET("&r");
        
        private String color;

        private FormatColors(String c)
        {
            color = c;
        }

        public String getColorCode()
        {
            return color;
        }

        public String getBukkitColorCode()
        {
            return color.replace("&", "\u00A7");
        }
    }
}
