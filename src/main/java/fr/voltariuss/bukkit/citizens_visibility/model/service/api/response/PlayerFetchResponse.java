package fr.voltariuss.bukkit.citizens_visibility.model.service.api.response;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Accessors(fluent = true, chain = true, makeFinal = true)
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class PlayerFetchResponse extends PlayerResponse {

  @NonNull private final List<Player> players;
}
