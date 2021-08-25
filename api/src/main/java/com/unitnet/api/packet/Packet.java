package com.unitnet.api.packet;

public interface Packet<T extends PacketHandler> {

  void send(final byte[] bytes);

}
