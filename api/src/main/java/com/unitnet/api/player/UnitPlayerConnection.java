package com.unitnet.api.player;

import com.unitnet.api.channel.UnitConnection;
import org.jetbrains.annotations.NotNull;

public interface UnitPlayerConnection extends UnitConnection {

  void sendPlayerPacket(final @NotNull PlayerPacket<?> packet);

}
