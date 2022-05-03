package fr.voltariuss.bukkit.citizens_visibility.model.entity;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.converter.UUIDConverter;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "citizens_visibility_player")
@ToString
@Accessors(fluent = true, chain = true, makeFinal = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class CitizenVisibility {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cv_id", nullable = false, updatable = false)
  private long id;

  @Column(name = "cv_player_uuid", nullable = false, unique = true, updatable = false)
  @Convert(converter = UUIDConverter.class)
  @Setter
  @NonNull
  private UUID playerUuid;

  @Column(name = "cv_npc_id")
  @Setter
  private int npcId;

  @Column(name = "cv_is_npc_visible")
  @Setter
  private boolean isNpcVisible;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof CitizenVisibility that)) {
      return false;
    }

    return new EqualsBuilder()
        .append(npcId, that.npcId)
        .append(isNpcVisible, that.isNpcVisible)
        .append(playerUuid, that.playerUuid)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(playerUuid)
        .append(npcId)
        .append(isNpcVisible)
        .toHashCode();
  }
}
