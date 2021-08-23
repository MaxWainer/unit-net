package com.maxwainer.unitnet.internal.option;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ImplOptions implements ServerOptions {

  private final Map<String, String> rawOptions;

  ImplOptions(final @NotNull String[] args) {
    this.rawOptions = Arrays.stream(args)
        .collect(Collectors.toMap(it -> it.split("\\s++")[0],
            it -> it.split("\\s++")[1]));
  }

  @Override
  public Map<String, String> getRawOptions() {
    return rawOptions;
  }

  @Override
  public <T> @Nullable T getOption(@NotNull String name, @NotNull Class<T> clazz) {
    final String option = rawOptions.get(name);

    if (option == null) {
      return null;
    }

    return clazz.cast(option);
  }

  @Override
  public <T> @Nullable T getOptionOr(@NotNull String name, @NotNull Class<T> clazz,
      @Nullable T or) {
    final T option = getOption(name, clazz);

    return option == null ? or : option;
  }

}
