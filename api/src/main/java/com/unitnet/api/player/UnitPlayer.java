package com.unitnet.api.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

public interface UnitPlayer {

  /**
   * Default {@link net.kyori.adventure.text.minimessage.MiniMessage}
   */
  MiniMessage DISCORD_LIKE_MINI_MESSAGE = MiniMessage.builder()
      .markdownFlavor(DiscordFlavor.get())
      .build();

  /**
   * Send to player message
   *
   * @param component Sending component
   */
  void message(final @NotNull Component component);

  /**
   * Send to player message with own mini-message parser
   *
   * @param miniMessage MiniMessage builder, can be applied own settings
   * @param message     Sending message
   */
  void message(final @NotNull MiniMessage miniMessage, final @NotNull String message);

  /**
   * Send to player message as string (Will be converted to {@link net.kyori.text.Component})
   *
   * @param message Sending message
   */
  default void message(final @NotNull String message) {
    message(Component.text(message));
  }

  /**
   * Send to player message as string with placeholders
   *
   * @param message      Sending message
   * @param placeholders Placeholders ({@link net.kyori.adventure.text.minimessage.Template})
   */
  default void message(final @NotNull String message, final @NotNull Template... placeholders) {
    message(DISCORD_LIKE_MINI_MESSAGE.parse(message, placeholders));
  }

  /**
   * Send title to player
   *
   * @param title Sending title
   */
  void title(final @NotNull Title title);

  /**
   * @return Player connection (For sending packets)
   */
  @NotNull UnitPlayerConnection getPlayerConnection();

}
