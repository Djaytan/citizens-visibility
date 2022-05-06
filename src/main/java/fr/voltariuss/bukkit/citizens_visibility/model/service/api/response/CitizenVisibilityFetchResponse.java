package fr.voltariuss.bukkit.citizens_visibility.model.service.api.response;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Accessors(fluent = true, chain = true, makeFinal = true)
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class CitizenVisibilityFetchResponse extends CitizenVisibilityResponse {

  private final CitizenVisibility citizenVisibility;

  @Builder
  public CitizenVisibilityFetchResponse(
      @NonNull ResponseType responseType,
      @NonNull String failureMessage,
      @Nullable CitizenVisibility citizenVisibility) {
    super(responseType, failureMessage);
    this.citizenVisibility = citizenVisibility;
  }

  public boolean isValuePresent() {
    return citizenVisibility != null;
  }

  public boolean isValueEmpty() {
    return citizenVisibility == null;
  }
}
