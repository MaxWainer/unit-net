package com.unitnet.implementation.server;

import java.net.URL;
import java.net.URLClassLoader;
import org.jetbrains.annotations.NotNull;

class PluginClassLoader extends URLClassLoader {

  public PluginClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, parent);
  }

}
