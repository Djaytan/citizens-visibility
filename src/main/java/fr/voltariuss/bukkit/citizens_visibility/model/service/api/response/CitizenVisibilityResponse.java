package fr.voltariuss.bukkit.citizens_visibility.model.service.api.response;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = true, makeFinal = true)
@Data
@RequiredArgsConstructor
public class CitizenVisibilityResponse {

  public enum ResponseType {
    SUCCESS,
    FAILURE
  }

  @NonNull private final ResponseType responseType;

  @NonNull private final String failureMessage;

  public CitizenVisibilityResponse() {
    this(ResponseType.SUCCESS, "");
  }

  public boolean success() {
    return responseType == ResponseType.SUCCESS;
  }
}
