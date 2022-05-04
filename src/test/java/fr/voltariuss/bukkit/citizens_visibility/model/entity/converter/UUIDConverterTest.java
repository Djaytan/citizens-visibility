package fr.voltariuss.bukkit.citizens_visibility.model.entity.converter;

import java.util.UUID;
import junit.framework.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("UUID converter")
class UUIDConverterTest {

  private final UUIDConverter uuidConverter;

  public UUIDConverterTest() {
    uuidConverter = new UUIDConverter();
  }

  @Test
  void givenRandomUuid_whenConvertToString_thenReturnUuidStringVersion() {
    UUID uuid = UUID.randomUUID();
    String uuidString = uuidConverter.convertToDatabaseColumn(uuid);
    Assert.assertEquals(uuid.toString(), uuidString);
  }

  @Test
  void givenNullUuid_whenConvertToString_thenReturnNullString() {
    String uuidString = uuidConverter.convertToDatabaseColumn(null);
    Assert.assertNull(uuidString);
  }

  @Test
  void givenRandomUuidString_whenConvertToUuid_thenReturnEqualsUuid() {
    UUID uuid = UUID.randomUUID();
    String uuidString = uuid.toString();
    UUID convertedBackUuid = uuidConverter.convertToEntityAttribute(uuidString);
    Assert.assertEquals(uuid, convertedBackUuid);
  }

  @Test
  void givenNullUuidString_whenConvertToUuid_thenReturnNullUuid() {
    UUID uuid = uuidConverter.convertToEntityAttribute(null);
    Assert.assertNull(uuid);
  }
}
