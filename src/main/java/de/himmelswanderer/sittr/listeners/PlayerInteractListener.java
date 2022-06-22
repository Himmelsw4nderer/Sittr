package de.himmelswanderer.sittr.listeners;

import de.himmelswanderer.sittr.Sittr;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.logging.Logger;

public class PlayerInteractListener implements Listener {
    private final Logger log;
    private final FileConfiguration config;

    public PlayerInteractListener(Sittr plugin) {
        //load logger
        this.log = plugin.getLogger();
        //load config
        this.config = plugin.getConfig();
    }

    //on use on block
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        //if player is right-clicking a block
        if(!event.getAction().isRightClick()) return;
        //if player or block in hand
        if(event.getItem() != null) return;

        //setting up variables
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        //if block is not null
        if(clickedBlock == null) return;

        //if the block is a chair selected in the config + get the block height
        double blockHeight = config.getDouble("blocks." + clickedBlock.getType().name(), -1.0);
        if (blockHeight == -1.0) return;

        //if the player has no tool
        if(!player.getInventory().getItemInMainHand().getType().isAir()) return;

        //if the two blocks above the clicked block are air
        if(!clickedBlock.getRelative(0,1,0).getType().isAir() || !clickedBlock.getRelative(0, 2, 0).getType().isAir()) return;

        //if player has permission to sit
        if(!player.hasPermission("sittr.sit")) return;

        //if the player is already sitting
        if(player.getVehicle() != null) return;

        blockHeight = blockHeight + config.getDouble("blockOffset", 0.0);

        //calculate entity position
        Location entityLocation = clickedBlock.getLocation().toCenterLocation().subtract(0, blockHeight,0);
        //create an invisible invulnerable armor stand with no gravity that is facing the same direction as the player
        Entity entity = player.getWorld().spawnEntity(entityLocation, EntityType.ARROW);
        entity.setRotation(player.getLocation().getYaw(), player.getLocation().getPitch());
        entity.setGravity(false);
        entity.setInvulnerable(true);
        entity.addPassenger(event.getPlayer());
    }
}
