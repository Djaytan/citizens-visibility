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
public class NamedEntitySpawnPacketListener extends PacketAdapter {

  private final CitizenVisibilityService citizenVisibilityService;

  @Inject
  public NamedEntitySpawnPacketListener(
      @NotNull Plugin plugin, @NotNull CitizenVisibilityService citizenVisibilityService) {
    super(plugin, ListenerPriority.HIGHEST, Server.NAMED_ENTITY_SPAWN);
    this.citizenVisibilityService = citizenVisibilityService;
  }

  @Override
  public void onPacketSending(PacketEvent event) {
    Entity namedEntitySpawned = event.getPacket().getEntityModifier(event).readSafely(0);
    NPC npc = CitizensAPI.getNPCRegistry().getNPC(namedEntitySpawned);

    if (npc == null) {
      return;
    }

    boolean isCitizenVisibleForPlayer =
        citizenVisibilityService.isCitizenVisibleForPlayer(
            event.getPlayer().getUniqueId(), npc.getId());

    if (!isCitizenVisibleForPlayer) {
      event.setCancelled(true);
    }
  }
}
