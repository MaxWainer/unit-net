package com.maxwainer.unitnet.internal.option;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ServerOptions {

  Map<String, String> getRawOptions();

  @Nullable <T> T getOption(final @NotNull String name, final @NotNull Class<T> clazz);

  @Nullable <T> T getOptionOr(final @NotNull String name, final @NotNull Class<T> clazz,
      final @Nullable T or);

  @NotNull
  default <T> T getOptionThrowable(final @NotNull String name, final @NotNull Class<T> clazz)
      throws UnknownOptionException {
    final T option = getOption(name, clazz);

    if (option == null) {
      throw new UnknownOptionException("Error while loading option");
    }

    return option;
  }

  static ServerOptions create(final @NotNull String[] rawArgs) {
    return new ImplOptions(rawArgs);
  }

}
