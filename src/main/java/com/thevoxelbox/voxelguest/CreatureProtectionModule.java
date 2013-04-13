package com.thevoxelbox.voxelguest;

import com.thevoxelbox.voxelguest.modules.BukkitEventWrapper;
import com.thevoxelbox.voxelguest.modules.MetaData;
import com.thevoxelbox.voxelguest.modules.Module;
import com.thevoxelbox.voxelguest.modules.ModuleConfiguration;
import com.thevoxelbox.voxelguest.modules.ModuleEvent;
import com.thevoxelbox.voxelguest.modules.Setting;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

/**
 * The Creature Protection Module was created to help maintain various creature
 * aspects, such as mob spawning. The Creature Protection Module offers extreme
 * amounts of customization based on config variables.
 *
 * Handles: Mob Spawning Mob Events
 */
@MetaData(name = "Creature Protection", description = "生物保护模块。")
public class CreatureProtectionModule extends Module {

    Server s = Bukkit.getServer();

    public CreatureProtectionModule()
    {
        super(CreatureProtectionModule.class.getAnnotation(MetaData.class));
    }

    class CreatureProtectionConfiguration extends ModuleConfiguration {

        @Setting("disable-bat-spawning") public boolean bat = false;
        @Setting("disable-blaze-spawning") public boolean blaze = false;
        @Setting("disable-cavespider-spawning") public boolean cavespider = false;
        @Setting("disable-chicken-spawning") public boolean chicken = false;
        @Setting("disable-cow-spawning") public boolean cow = false;
        @Setting("disable-creeper-spawning") public boolean creeper = false;
        @Setting("disable-enderman-spawning") public boolean enderman = false;
        @Setting("disable-enderdragon-spawning") public boolean enderdragon = false;
        @Setting("disable-ghast-spawning") public boolean ghast = false;
        @Setting("disable-giant-spawning") public boolean giant = false;
        @Setting("disable-irongolem-spawning") public boolean irongolem = false;
        @Setting("disable-magmacube-spawning") public boolean magmacube = false;
        @Setting("disable-mushroomcow-spawning") public boolean mushroomcow = false;
        @Setting("disable-ocelot-spawning") public boolean ocelot = false;
        @Setting("disable-pig-spawning") public boolean pig = false;
        @Setting("disable-pigzombie-spawning") public boolean pigzombie = false;
        @Setting("disable-sheep-spawning") public boolean sheep = false;
        @Setting("disable-silverfish-spawning") public boolean silverfish = false;
        @Setting("disable-skeleton-spawning") public boolean skeleton = false;
        @Setting("disable-slime-spawning") public boolean slime = false;
        @Setting("disable-snowman-spawning") public boolean snowman = false;
        @Setting("disable-spider-spawning") public boolean spider = false;
        @Setting("disable-squid-spawning") public boolean squid = false;
        @Setting("disable-villager-spawning") public boolean villager = false;
        @Setting("disable-witch-spawning") public boolean witch = false;
        @Setting("disable-wither-spawning") public boolean wither = false;
        @Setting("disable-wolf-spawning") public boolean wolf = false;
        @Setting("disable-zombie-spawning") public boolean zombie = false;

        public CreatureProtectionConfiguration(CreatureProtectionModule parent)
        {

            super(parent);
            
            
        }
    }

    @Override
    public void enable()
    {
        setConfiguration(new CreatureProtectionConfiguration(this));
    }

    @Override
    public String getLoadMessage()
    {
        return "生物保护模块读取完毕。";
    }

    @Override
    public void disable()
    {
        // Nothing special
    }

    /*
     * Creature Protection - CreatureSpawn Event Written by: Razorcane
     *
     * Handles creature spawning event.
     */
    @ModuleEvent(event = CreatureSpawnEvent.class)
    public void onCreatureSpawn(BukkitEventWrapper wrapper)
    {
    	
        CreatureSpawnEvent event = (CreatureSpawnEvent) wrapper.getEvent();
        EntityType mob = event.getEntityType();
        SpawnReason reason = event.getSpawnReason();
        if (reason != SpawnReason.CUSTOM) {
            switch (mob) {
                case BAT:
                    if (getConfiguration().getBoolean("disable-bat-spawning")) {
                        event.setCancelled(true);
                        return;
                    }
                case BLAZE:
                    if (getConfiguration().getBoolean("disable-blaze-spawning")) {
                        event.setCancelled(true);
                        return;
                    }
                case CAVE_SPIDER:
                    if (getConfiguration().getBoolean("disable-cavespider-spawning")) {
                        event.setCancelled(true);
                        return;
                    }
                case CHICKEN:
                    if (getConfiguration().getBoolean("disable-chicken-spawning")) {
                        event.setCancelled(true);
                        return;
                    }
                case COW:
                    if (getConfiguration().getBoolean("disable-cow-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case CREEPER:
                    if (getConfiguration().getBoolean("disable-creeper-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case ENDERMAN:
                    if (getConfiguration().getBoolean("disable-enderman-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case ENDER_DRAGON:
                    if (getConfiguration().getBoolean("disable-enderdragon-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case IRON_GOLEM:
                    if (getConfiguration().getBoolean("disable-irongolem-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case GHAST:
                    if (getConfiguration().getBoolean("disable-ghast-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case GIANT:
                    if (getConfiguration().getBoolean("disable-giant-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case MAGMA_CUBE:
                    if (getConfiguration().getBoolean("disable-magmacube-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case MUSHROOM_COW:
                    if (getConfiguration().getBoolean("disable-mushroomcow-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case OCELOT:
                    if (getConfiguration().getBoolean("disable-ocelot-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case PIG:
                    if (getConfiguration().getBoolean("disable-pig-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case PIG_ZOMBIE:
                    if (getConfiguration().getBoolean("disable-pigzombie-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case SHEEP:
                    if (getConfiguration().getBoolean("disable-sheep-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case SILVERFISH:
                    if (getConfiguration().getBoolean("disable-silverfish-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case SKELETON:
                    if (getConfiguration().getBoolean("disable-skeleton-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case SLIME:
                    if (getConfiguration().getBoolean("disable-slime-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case SNOWMAN:
                    if (getConfiguration().getBoolean("disable-snowman-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case SPIDER:
                    if (getConfiguration().getBoolean("disable-spider-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case SQUID:
                    if (getConfiguration().getBoolean("disable-squid-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case VILLAGER:
                    if (getConfiguration().getBoolean("disable-villager-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case WITCH:
                    if (getConfiguration().getBoolean("disable-witch-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case WOLF:
                    if (getConfiguration().getBoolean("disable-wolf-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case WITHER:
                    if (getConfiguration().getBoolean("disable-wither-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
                case ZOMBIE:
                    if (getConfiguration().getBoolean("disable-zombie-spawning")) {
                        event.setCancelled(true);
                        return;
                        
                    }
            }
        }
    }
}
