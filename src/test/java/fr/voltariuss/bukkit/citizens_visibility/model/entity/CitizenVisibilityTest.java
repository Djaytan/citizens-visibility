package fr.voltariuss.bukkit.citizens_visibility.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CitizenVisibility entity")
class CitizenVisibilityTest {

  @Test
  void equalsAndHashCodeContract() {
    EqualsVerifier.forClass(CitizenVisibility.class).verify();
  }
}
