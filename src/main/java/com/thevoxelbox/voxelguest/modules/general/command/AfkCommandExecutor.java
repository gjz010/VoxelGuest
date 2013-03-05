package com.thevoxelbox.voxelguest.modules.general.command;

import com.thevoxelbox.voxelguest.modules.general.GeneralModule;
import com.thevoxelbox.voxelguest.modules.general.GeneralModuleConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author TheCryoknight
 */
public class AfkCommandExecutor implements CommandExecutor
{
    private final GeneralModule module;
    private final List<String> afkMsgs = new ArrayList<>();

    /**
     * Creates a new /afk command executor
     *
     * @param generalModule The owning parent module.
     */
    public AfkCommandExecutor(final GeneralModule generalModule)
    {
        this.module = generalModule;
        this.afkMsgs.add("is using the space toilet.");
        this.afkMsgs.add("was attacked by a Derpie Bird.");
        this.afkMsgs.add("went out to Oinkies for a pulled pork sandwich.");
        this.afkMsgs.add("was attacked by a wild dragon and is incapacitated!");
        this.afkMsgs.add("went to build a turtlefence on Quercas.");
        this.afkMsgs.add("is eating Fibonachos.... mm, CHEESY.");
        this.afkMsgs.add("is consulting the VoxelWiki.");
        this.afkMsgs.add("is eating Negative Quantum Cake until they get a stomachache.");
        this.afkMsgs.add("is celebrating Ridgeday!");
        this.afkMsgs.add("is creating a Ridgememe.");
        this.afkMsgs.add("is being attacked by RidgeClones.");
        this.afkMsgs.add("is creating a Smurfmeme!");
        this.afkMsgs.add("is getting vaccinated for Cobbles.");
        this.afkMsgs.add("is munching on DobaCrackaz.");
        this.afkMsgs.add("is licking Neutron Rocky Road Ice Cream.");
        this.afkMsgs.add("is cutting a Pai Pie.");
        this.afkMsgs.add("is enjoying Poffertjes.");
        this.afkMsgs.add("is chugging VoxeLager.");
        this.afkMsgs.add("is hugging a cactus named Steve.");
        this.afkMsgs.add("is ducking under cover to avoid a Pangean Flying Cactus attack.");
        this.afkMsgs.add("is cowering in fear of ridgedog.");
        this.afkMsgs.add("crashed the server and is running away from ridgedog.");
        this.afkMsgs.add("is being attacked by VoxelSmurfs.");
        this.afkMsgs.add("is praying to [GOD].");
        this.afkMsgs.add("is taking a a bubble bath.");
        this.afkMsgs.add("is programming plugins whilst slurping powered on potato soup.");
        this.afkMsgs.add("is not here currently due to being away from the keyboard so LEAVE THEM ALONE!");
        this.afkMsgs.add("is hanging out at the Assassin’s Guild.");
        this.afkMsgs.add("is loving a cat and loving to run right now.");
        this.afkMsgs.add("is shoveling a bit of [GOD]’s poo off of the Chocobo Islands.");
        this.afkMsgs.add("just derped.");
        this.afkMsgs.add("got all derpy.");
        this.afkMsgs.add("is contemplating the meaning of life.");
        this.afkMsgs.add("is looking for TedStone deposits in their underwear.");
        this.afkMsgs.add("is thinking about cats again.");
        this.afkMsgs.add("is trying to pull a fishing bobber off his face.");
        this.afkMsgs.add("is installing The Voxel Box Minecraft Modpack.");
        this.afkMsgs.add("is busy trying to get onto Buxville.");
        this.afkMsgs.add("is going into SPAAAAAAAAAAAAAAAACE.");
        this.afkMsgs.add("is going to burn your house down... with lemons.");
        this.afkMsgs.add("is stealing plugins from Styx City.");
        this.afkMsgs.add("is talking in CAPS CAPS CAPS CAPS.");
        this.afkMsgs.add("is taking a squat.");
        this.afkMsgs.add("has lost their mind.");
        this.afkMsgs.add("is stealing Negative Quantum Cake.");
        this.afkMsgs.add("is building a box.");
        this.afkMsgs.add("is contemplating the meaning of assfedora.");
        this.afkMsgs.add("is depositing a gilded turd.");
        this.afkMsgs.add("has lost the game so is taking a time out.");
        this.afkMsgs.add("is taking a gander.");
        this.afkMsgs.add("has wandered off.");
        this.afkMsgs.add("hurrrrr durrrrrrr te derp dur.");
        this.afkMsgs.add("is not AFK and wants everybody to not know it.");
        this.afkMsgs.add("is going WEST. ('cause life is peaceful there!)");
        this.afkMsgs.add("left to kick ass and chew bubblegum.");
        this.afkMsgs.add("left to humm the Ghostbusters theme tune in his head.");
        this.afkMsgs.add("just thought about cats.");
        this.afkMsgs.add("is thinking about how many cats don’t have a home.");
        this.afkMsgs.add("wants cats in a little basket. with little bow ties.");
        this.afkMsgs.add("left to crash giant spaceships into makeshift landscapes.");
        this.afkMsgs.add("got bored of minecraft but doesn’t want to log off.");
        this.afkMsgs.add("is building a Rattlesnake fence.");
        this.afkMsgs.add("is adding more torches to Citadel City.");
        this.afkMsgs.add("is travelling through time and space.");
        this.afkMsgs.add("wishes they were piderman.");
        this.afkMsgs.add("is building organix WITH LINES.");
        this.afkMsgs.add("is away, griefing their shorts.");
        this.afkMsgs.add("is making friends over in StyxCity.");
        this.afkMsgs.add("is looking at pictures of Pandas on Google images.");
        this.afkMsgs.add("is looking at pictures of giraffe on Google images.");
        this.afkMsgs.add("is bugging ridge about putting back their city.");
        this.afkMsgs.add("is ignoring the city ridge finally put back for them.");
        this.afkMsgs.add("is feeling bad about his actions.");
        this.afkMsgs.add("is riding the comet.");
        this.afkMsgs.add("is lifting. Bro!");
        this.afkMsgs.add("is riding a kirk.");
        this.afkMsgs.add("has realized that there are no more AFK messages to make.");
        this.afkMsgs.add("is getting an explanation about performer system in VoxelSniper.");
        this.afkMsgs.add("is spoiling your childhood dreams with George Lucas.");
        this.afkMsgs.add("is contemplating the meaning of life.");
        this.afkMsgs.add("has stepped through the stargate.");
        this.afkMsgs.add("is making an airplane.");
        this.afkMsgs.add("is wrestling with Amazon.com customer support.");
        this.afkMsgs.add("is read SpecFic novels in the Aerios library.");
        this.afkMsgs.add("is drunk and is unable to communicate with the rest of the server.");
        this.afkMsgs.add("is trying to steal Feathers from Featherblade's blades.");
        this.afkMsgs.add("has farted and realised they followed through.");
        this.afkMsgs.add("is supporting Team Nexus with a generous Paypal donation.");
        this.afkMsgs.add("will be back in a flash.");
        this.afkMsgs.add("is building a nuclear bunker.");
        this.afkMsgs.add("has fallen asleep, dip their hand in some warm water, quick!");
        this.afkMsgs.add("is looking for potatoes.");
        this.afkMsgs.add("is a potato.");
        this.afkMsgs.add("has been turned into a robot and is powered by a potato.");
        this.afkMsgs.add("just flew off the wolf.");
        this.afkMsgs.add("is no longer subject to the gravitational constant!");
        this.afkMsgs.add("smells like a recently-built sewer.");
        this.afkMsgs.add("chews the end of their Magic Paintbrush, that's why their mouth always looks so messy.");
        this.afkMsgs.add("is the one who clicked on your build with the Auto-Shoveler 9001.");
        this.afkMsgs.add("uses their Graviton Sledge for VoxelDentistry.");
        this.afkMsgs.add("uses a grimy copy of the Voxel Times as a blanket.");
        this.afkMsgs.add("has been subpoenaed by the VoxelCourt!");
        this.afkMsgs.add("has a date tonight with a RidgeClone.");
        this.afkMsgs.add("is playing the Slave of the Coal Mine tabletop RPG with [IGNORANCE].");
        this.afkMsgs.add("never understands anything [PHILOSOPHY] says.");
        this.afkMsgs.add("is doing it for the [SCIENCE].");
        this.afkMsgs.add("just found a Voxar in their doodie and is trying to figure out how to extract it.");
        this.afkMsgs.add("is tagging the walls of the Nexus with their name.");
        this.afkMsgs.add("makes annual donations to the Hug-a-Cactus foundation.");
        this.afkMsgs.add("is choking on their dinner at Flux's Chix-on-a-stick.");
        this.afkMsgs.add("thinks cyborgs are kinda sexy.");
        this.afkMsgs.add("thinks the Big Shell has a snail inside it.");
        this.afkMsgs.add("is all hopped up on Diet Dr. Smurfy");
        this.afkMsgs.add("loses at Whack-A-Mole EVERY TIME!");
        this.afkMsgs.add("just got 3 TNT on the slot machine.");
        this.afkMsgs.add("is splashing around in a VoxelFountain.");
        this.afkMsgs.add("thinks they're better than you at MagnetoSpleef.");
        this.afkMsgs.add("is doing the booty do");
        this.afkMsgs.add("Going to ride a carnival ride.");
        this.afkMsgs.add("is playing OSU!");
        this.afkMsgs.add("Eating a shmufit.");
        this.afkMsgs.add("is look pretty when they cry.");
        this.afkMsgs.add("bitches don't know about my trapezoid.");
        this.afkMsgs.add("Tentacles everywhere!");
        this.afkMsgs.add("is sniffing mint");
        this.afkMsgs.add("hand is a doliphin!");
        this.afkMsgs.add("The club can't even handle me right now.");
        this.afkMsgs.add("AFKers gonna AFK");
        this.afkMsgs.add("Affiliating Frequent Kicks");
        this.afkMsgs.add("I got a rock.");
        this.afkMsgs.add("Shut your mouth Rose, no one wants to hear that raddle.");
        this.afkMsgs.add("Deloris ain't got nothing on me.");
        this.afkMsgs.add("is fighting dragons in the kitchen.");
        this.afkMsgs.add("Booty booty booty booty rockin' everywhere.");
        this.afkMsgs.add("is going the West West!");


    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            this.module.getAfkManager().toggleAfk(player);
            if (this.module.getAfkManager().isPlayerAfk(player))
            {
                if (args.length != 0)
                {
                    String afkMsg = "";
                    for (final String arg : args)
                    {
                        afkMsg += " ";
                        afkMsg += arg;
                    }
                    Bukkit.broadcastMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.DARK_GRAY + afkMsg);
                    return true;
                }
                if (((GeneralModuleConfiguration) this.module.getConfiguration()).isRandomAfkMsgs())
                {
                    Bukkit.broadcastMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.DARK_GRAY + " " + this.getAfkMsg());
                }
                else
                {
                    Bukkit.broadcastMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.DARK_GRAY + " has gone AFK");
                }
            }
            return true;
        }
        return false;
    }

    private String getAfkMsg()
    {
        final Random rand = new Random();
        return this.afkMsgs.get(rand.nextInt(this.afkMsgs.size()));
    }

}
