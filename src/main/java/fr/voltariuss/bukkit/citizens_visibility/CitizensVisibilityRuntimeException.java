package fr.voltariuss.bukkit.citizens_visibility;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CitizensVisibilityRuntimeException extends RuntimeException {

  public CitizensVisibilityRuntimeException(@NotNull String message) {
    super(message);
  }

  public CitizensVisibilityRuntimeException(@NotNull String message, @Nullable Throwable cause) {
    super(message, cause);
  }
}
