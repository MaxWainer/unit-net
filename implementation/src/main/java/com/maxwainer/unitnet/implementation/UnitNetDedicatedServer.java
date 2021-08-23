package com.maxwainer.unitnet.implementation;

import com.maxwainer.unitnet.api.UnitNetServer;
import com.maxwainer.unitnet.internal.option.ServerOptions;
import com.maxwainer.unitnet.internal.option.UnknownOptionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class UnitNetDedicatedServer extends AbstractDedicatedServer implements UnitNetServer {

  public static final Logger LOGGER = LogManager.getLogger(UnitNetDedicatedServer.class);

  public static void main(String... args) throws UnknownOptionException {
    final ServerOptions options = ServerOptions.create(args);

  }

}
