package com.unitnet.implementation.server.properties;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ParsingException;

public class ProxyProperties {

  private final CommentedConfigurationNode config;
  private final CommentedConfigurationNode serverConfig;

  public ProxyProperties(final @NotNull Path config, final @NotNull Path serverConfig) throws ConfigurateException {
    final HoconConfigurationLoader.Builder builder =
        HoconConfigurationLoader.builder().emitComments(true).emitJsonCompatible(true);

    this.config = builder.path(config).build().load();
    this.serverConfig = builder.path(serverConfig).build().load();
  }

  @NotNull
  public CommentedConfigurationNode getConfig() {
    return config;
  }

  @NotNull
  public CommentedConfigurationNode getServerConfig() {
    return serverConfig;
  }
}
