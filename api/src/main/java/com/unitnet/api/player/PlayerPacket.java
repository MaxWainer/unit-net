package com.unitnet.api.player;

import com.unitnet.api.packet.Packet;
import com.unitnet.api.packet.PacketHandler;

public interface PlayerPacket<T extends PacketHandler> extends Packet<T> {

}
