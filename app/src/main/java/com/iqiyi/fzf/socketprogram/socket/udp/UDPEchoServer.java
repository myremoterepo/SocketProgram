package com.iqiyi.fzf.socketprogram.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by fzf on 17-9-22.
 * DatagramPacket接收的最大数据量为65507，超出会被丢弃
 */

public class UDPEchoServer {
    private static final int MAX_SIZE = 255;

    /**
     * 服务终端开启
     * @param serverPort 服务器套接字的本地端口
     * @throws IOException
     */
    public void doTask(int serverPort) throws IOException {
        DatagramSocket socket = new DatagramSocket(serverPort);//显示设置本地的端口号，接收到客户端的数据报文后，可以获取到客户端的地址和端口
        DatagramPacket packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);//UDP套接字的消息包含在数据报文中，接收和发送信息

        //UDP套接字为所有的通信使用同一个套接字
        //TCP的accept会返回一个新的套接字
        while (true){
            socket.receive(packet);//阻塞等待，由于没有连接，数据包可来自不同的客户端
            System.out.println("handling client at " + packet.getAddress().getHostAddress() + " on port " + packet.getPort());//获取源地址和端口，作为回馈的目的地址接端口
            socket.send(packet);//发送回馈信息，用了同一个packet，发送的数据量不会改变
            packet.setLength(MAX_SIZE);
        }

    }

}
