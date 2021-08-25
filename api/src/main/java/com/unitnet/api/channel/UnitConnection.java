package com.unitnet.api.channel;

import com.unitnet.api.channel.connection.pipe.Pipe;
import org.jetbrains.annotations.NotNull;

public interface UnitConnection {

  @NotNull Pipe getConnectionPipe();

}
