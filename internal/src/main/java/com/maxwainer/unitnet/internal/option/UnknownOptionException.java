package com.maxwainer.unitnet.internal.option;

import org.jetbrains.annotations.NotNull;

public final class UnknownOptionException extends Throwable {

  public UnknownOptionException(final @NotNull String message) {
    super(message);
  }

}
