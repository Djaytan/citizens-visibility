package fr.voltariuss.bukkit.citizens_visibility.model.entity;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.converter.UUIDConverter;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "citizen_visibility_player")
@ToString
@EqualsAndHashCode
@Accessors(fluent = true, chain = true, makeFinal = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Player {

  @Id
  @Column(name = "cv_player_uuid", nullable = false, unique = true, updatable = false)
  @Convert(converter = UUIDConverter.class)
  @NonNull
  private UUID playerUuid;

  @Column(name = "cv_player_name")
  @Setter
  @Nullable
  private String playerName;
}
