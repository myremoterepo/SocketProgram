package com.iqiyi.fzf.socketprogram.socket.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by fzf on 17-9-25.
 * 多播
 */

public class VoteMulticastReceiver {
    private static final int MAX_RCV_SIZE = 100;

    public void doTask(String address, int port) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(address);

        if (!inetAddress.isMulticastAddress()){
            throw new IllegalArgumentException("not a multicast address");
        }

        MulticastSocket multicastSocket = new MulticastSocket(port);
        multicastSocket.joinGroup(inetAddress);//套接字加入多播组，一个套接字可以同时加入多个多播组

        DatagramPacket packet = new DatagramPacket(new byte[MAX_RCV_SIZE], MAX_RCV_SIZE);

        multicastSocket.receive(packet);

        System.out.println("receive " + new String(packet.getData()));

        multicastSocket.close();
    }
}
