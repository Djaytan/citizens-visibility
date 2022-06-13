package fr.voltariuss.bukkit.citizens_visibility.plugin;

import com.comphenix.protocol.ProtocolManager;
import fr.voltariuss.bukkit.citizens_visibility.controller.listener.packet.NamedEntitySpawnPacketListener;
import fr.voltariuss.bukkit.citizens_visibility.controller.listener.packet.SpawnEntityLivingPacketListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PacketListenerRegister {

  private final ProtocolManager protocolManager;
  private final NamedEntitySpawnPacketListener namedEntitySpawnPacketListener;
  private final SpawnEntityLivingPacketListener spawnEntityLivingPacketListener;

  @Inject
  public PacketListenerRegister(
      @NotNull ProtocolManager protocolManager,
      @NotNull NamedEntitySpawnPacketListener namedEntitySpawnPacketListener,
      @NotNull SpawnEntityLivingPacketListener spawnEntityLivingPacketListener) {
    this.protocolManager = protocolManager;
    this.namedEntitySpawnPacketListener = namedEntitySpawnPacketListener;
    this.spawnEntityLivingPacketListener = spawnEntityLivingPacketListener;
  }

  public void registerListeners() {
    protocolManager.addPacketListener(namedEntitySpawnPacketListener);
    protocolManager.addPacketListener(spawnEntityLivingPacketListener);
  }
}
