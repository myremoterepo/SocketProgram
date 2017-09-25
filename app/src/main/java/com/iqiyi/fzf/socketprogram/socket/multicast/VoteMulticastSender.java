package com.iqiyi.fzf.socketprogram.socket.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by fzf on 17-9-25.
 */

public class VoteMulticastSender {
    public static final int CANDIDATED = 475;
    private static final int TTL = 1;

    public void doTask(String destAddr, int destPort, String msg) throws IOException {
        MulticastSocket multicastSocket = new MulticastSocket();
        multicastSocket.setTimeToLive(TTL);

        DatagramPacket datagramPacket = new DatagramPacket(msg.getBytes(), msg.length(), InetAddress.getByName(destAddr), destPort);
        System.out.println("sending message " + msg + " length " + msg.length());
        multicastSocket.send(datagramPacket);

        multicastSocket.close();

    }
}
