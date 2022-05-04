/*
 * Copyright (c) 2022 - Loïc DUBOIS-TERMOZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.voltariuss.bukkit.citizens_visibility.model.entity.converter;

import java.util.UUID;
import javax.persistence.AttributeConverter;
import org.jetbrains.annotations.Nullable;

public class UUIDConverter implements AttributeConverter<UUID, String> {

  @Override
  public @Nullable String convertToDatabaseColumn(@Nullable UUID uuid) {
    return uuid != null ? uuid.toString() : null;
  }

  @Override
  public @Nullable UUID convertToEntityAttribute(@Nullable String uuidString) {
    return uuidString != null ? UUID.fromString(uuidString) : null;
  }
}
