package fr.voltariuss.bukkit.citizens_visibility.controller.listener.packet;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.CitizenVisibilityService;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public class SpawnEntityLivingPacketListener extends PacketAdapter {

  private final CitizenVisibilityService citizenVisibilityService;

  @Inject
  public SpawnEntityLivingPacketListener(
      @NotNull Plugin plugin, @NotNull CitizenVisibilityService citizenVisibilityService) {
    super(plugin, ListenerPriority.HIGHEST, Server.SPAWN_ENTITY);
    this.citizenVisibilityService = citizenVisibilityService;
  }

  @Override
  public void onPacketSending(PacketEvent event) {
    Entity livingEntityToSpawn = event.getPacket().getEntityModifier(event).readSafely(0);
    NPC npc = CitizensAPI.getNPCRegistry().getNPC(livingEntityToSpawn);

    if (npc == null) {
      return;
    }

    // Sorry for that
    boolean isCitizenVisibleForPlayer =
        citizenVisibilityService
            .isCitizenVisibleForPlayer(event.getPlayer().getUniqueId(), npc.getId())
            .join();

    if (!isCitizenVisibleForPlayer) {
      event.setCancelled(true);
    }
  }
}
