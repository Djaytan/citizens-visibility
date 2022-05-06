package fr.voltariuss.bukkit.citizens_visibility.model.service.api.response;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Accessors(fluent = true, chain = true, makeFinal = true)
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class CitizenVisibilityFetchResponse extends CitizenVisibilityResponse {

  private final CitizenVisibility citizenVisibility;

  public boolean isValuePresent() {
    return citizenVisibility != null;
  }

  public boolean isValueEmpty() {
    return citizenVisibility == null;
  }
}
