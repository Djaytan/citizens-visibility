package fr.voltariuss.bukkit.citizens_visibility.model.service.api.parameter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Builder
@Accessors(fluent = true, chain = true, makeFinal = true)
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class PlayerRegisterResponse {

  public enum ResponseType {
    PLAYER_REGISTERED,
    PLAYER_NAME_UPDATED,
    NOTHING
  }

  @NonNull private final ResponseType responseType;
  @Nullable private final String oldPlayerName;
  @Nullable private final String newPlayerName;
}
