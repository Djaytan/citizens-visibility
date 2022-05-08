package fr.voltariuss.bukkit.citizens_visibility.plugin;

import com.comphenix.protocol.ProtocolManager;
import fr.voltariuss.bukkit.citizens_visibility.controller.listener.packet.SpawnLivingEntityPacketListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PacketListenerRegister {

  private final ProtocolManager protocolManager;
  private final SpawnLivingEntityPacketListener spawnLivingEntityPacketListener;

  @Inject
  public PacketListenerRegister(
      @NotNull ProtocolManager protocolManager,
      @NotNull SpawnLivingEntityPacketListener spawnLivingEntityPacketListener) {
    this.protocolManager = protocolManager;
    this.spawnLivingEntityPacketListener = spawnLivingEntityPacketListener;
  }

  public void registerListeners() {
    protocolManager.addPacketListener(spawnLivingEntityPacketListener);
  }
}
