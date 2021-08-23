package com.maxwainer.unitnet.api.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

public interface UnitPlayer {

  void message(final @NotNull Component component);

  void title(final @NotNull Title title);

}
