package fr.voltariuss.bukkit.citizens_visibility.plugin;

import com.comphenix.protocol.ProtocolManager;
import fr.voltariuss.bukkit.citizens_visibility.controller.listener.packet.NamedEntitySpawnPacketListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PacketListenerRegister {

  private final ProtocolManager protocolManager;
  private final NamedEntitySpawnPacketListener namedEntitySpawnPacketListener;

  @Inject
  public PacketListenerRegister(
      @NotNull ProtocolManager protocolManager,
      @NotNull NamedEntitySpawnPacketListener namedEntitySpawnPacketListener) {
    this.protocolManager = protocolManager;
    this.namedEntitySpawnPacketListener = namedEntitySpawnPacketListener;
  }

  public void registerListeners() {
    protocolManager.addPacketListener(namedEntitySpawnPacketListener);
  }
}
