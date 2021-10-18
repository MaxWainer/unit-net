package com.unitnet.internal.option;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ServerOptions {

  static ServerOptions create(final @NotNull String[] rawArgs) {
    return new ImplOptions(rawArgs);
  }

  Map<String, String> getRawOptions();

  @Nullable
  <T> T getOption(final @NotNull String name, final @NotNull Class<T> clazz);

  @Nullable
  <T> T getOptionOr(
      final @NotNull String name, final @NotNull Class<T> clazz, final @Nullable T or);

  default boolean has(final @NotNull String name) {
    return getOption(name, Object.class) != null;
  }

  default boolean hasAndEquals(final @NotNull String name, final @NotNull String check) {
    return has(name) && getOption(name, String.class).equals(check);
  }

  @NotNull
  default <T> T getOptionThrowable(final @NotNull String name, final @NotNull Class<T> clazz)
      throws UnknownOptionException {
    final T option = getOption(name, clazz);

    if (option == null) {
      throw new UnknownOptionException("Error while loading option");
    }

    return option;
  }
}
