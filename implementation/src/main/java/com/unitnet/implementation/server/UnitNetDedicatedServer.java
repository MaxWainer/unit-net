package com.unitnet.implementation.server;

import com.google.common.base.Preconditions;
import com.unitnet.api.UnitNetServer;
import com.unitnet.implementation.server.properties.ProxyProperties;
import com.unitnet.internal.option.ServerOptions;
import com.unitnet.internal.option.UnknownOptionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessageFactory;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ParsingException;

public final class UnitNetDedicatedServer extends AbstractDedicatedServer
    implements UnitNetServer, Runnable {

  public static final Logger LOGGER =
      new SimpleLogger(
          UnitNetDedicatedServer.class.getSimpleName(),
          Level.ALL,
          true,
          true,
          true,
          true,
          "dd.MM.YYYY HH:mm:ss",
          new FormattedMessageFactory(),
          PropertiesUtil.getProperties(),
          System.out);

  private final AtomicBoolean running = new AtomicBoolean(false);

  private final File proxyFilesFolder;
  private final ProxyProperties proxyProperties;

  UnitNetDedicatedServer(final @NotNull File proxyFilesFolder) {
    Preconditions.checkArgument(
        !proxyFilesFolder.isDirectory(), "File %s, is not folder!", proxyFilesFolder);
    this.proxyFilesFolder = proxyFilesFolder;

    tryLoad0();

    try {
      this.proxyProperties =
          new ProxyProperties(tryLoadProxyFile("config.conf"), tryLoadProxyFile("servers.conf"));
    } catch (IOException | URISyntaxException ex) {
      LOGGER.error("Error while loading files!", ex);
      throw new RuntimeException(ex);
    }
  }

  public static void main(String... args) throws UnknownOptionException, ParsingException {
    final ServerOptions options = ServerOptions.create(args);

    if (options.has("--useRedis")) {
      LOGGER.info("Using redis for caching!");
    }

    final File folder =
        new File(options.has("--directory") ? options.getOption("--directory", String.class) : "/");

    final UnitNetDedicatedServer server = new UnitNetDedicatedServer(folder);

    server.run();
  }

  private void tryLoad0() {
    LOGGER.info("Running server on {}", System.getProperty("os.name", "Unknown"));

    if (!proxyFilesFolder.exists()) proxyFilesFolder.mkdirs();

    LOGGER.info("Starting server...");
  }

  void startServer() throws ParsingException {
    final long start = System.currentTimeMillis();

    final CommentedConfigurationNode config = proxyProperties.getConfig();

    LOGGER.info(
        "Running on: [ Host = {} / Port = {} ]",
        config.node("general", "host").getString(),
        config.node("general", "port").getInt());

    LOGGER.info("Loaded in {}ms!", System.currentTimeMillis() - start);
  }

  private Path tryLoadProxyFile(final @NotNull String name) throws IOException, URISyntaxException {
    final InputStream inputStream =
        Objects.requireNonNull(
            UnitNetDedicatedServer.class.getResourceAsStream(File.separator + name),
            String.format("Unknown file %s!", name));

    final Path outPath = Paths.get(new File(proxyFilesFolder, name).toURI());

    Files.copy(inputStream, outPath, StandardCopyOption.REPLACE_EXISTING);

    return outPath;
  }

  @Override
  public void run() {
    try {
      startServer();

      running.set(true);

      while (running.get()) {
        LOGGER.info("Server is running right now!");
      }
    } catch (Throwable throwable) {
      LOGGER.error("Error while running server!", throwable);
    }
  }
}
