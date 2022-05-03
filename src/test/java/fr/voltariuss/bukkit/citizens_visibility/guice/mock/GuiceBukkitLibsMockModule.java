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

package fr.voltariuss.bukkit.citizens_visibility.guice.mock;

import co.aikar.commands.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;

public class GuiceBukkitLibsMockModule extends AbstractModule {

  @Provides
  @Singleton
  public @NotNull MiniMessage provideMiniMessage() {
    return Mockito.mock(MiniMessage.class);
  }

  @Provides
  @Singleton
  public @NotNull PaperCommandManager provideAcfPaperCommandManager() {
    return Mockito.mock(PaperCommandManager.class);
  }
}
