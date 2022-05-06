package fr.voltariuss.bukkit.citizens_visibility.model.service.api.response;

import lombok.AllArgsConstructor;
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
@EqualsAndHashCode
@AllArgsConstructor
public class CitizenVisibilityResponse {

  public enum ResponseType {
    SUCCESS,
    BAD_INPUT,
    SERVER_ERROR
  }

  @NonNull private final ResponseType responseType;

  @NonNull private final String failureMessage;

  public boolean isSuccessful() {
    return responseType == ResponseType.SUCCESS;
  }

  public boolean isFailure() {
    return responseType != ResponseType.SUCCESS;
  }
}
