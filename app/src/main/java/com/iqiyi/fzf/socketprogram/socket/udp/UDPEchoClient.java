package com.iqiyi.fzf.socketprogram.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by fzf on 17-9-21.
 */

public class UDPEchoClient {
    private static final int TIME_OUT = 3000;
    private static final int MAX_TRIES = 5;

    public void doTask(InetAddress serverAddress, int serverPort, String message) throws IOException {
        byte[] bytesToSend = message.getBytes();//获取字节数组

        DatagramSocket socket = new DatagramSocket();//自动获取本地地址和可用端口号，将数据报文发送给任何UDP套接字
        socket.setSoTimeout(TIME_OUT);//设置超时时间，控制receive的最长阻塞时间

        DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, serverPort);//发送的数据报文，指定数据，目的地址，目的端口
        DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);//接收的数据报文，指定存放数据的字节数组。地址和端口会从receive获取
        int tries = 0;

        boolean receiveResponse = false;

        do {
            socket.send(sendPacket);//发送数据报文
            try {
                socket.receive(receivePacket);//receive阻塞等待，直到收到数据或者超时
                if (!receivePacket.getAddress().equals(serverAddress)){//数据报文可能来自任何地址，验证接收的地址和端口是否是指定的服务器地址和端口号
                    throw new IOException("receive packet from an unknown source");
                }

                receiveResponse = true;//循环标记，接收到数据会退出循环
                System.out.println("echo receive string " + new String(receivePacket.getData()));
            }catch (Exception e){
                tries += 1;
                System.out.println("Time out, " + (MAX_TRIES -tries) + " more tries...");
            }
        } while (!receiveResponse && tries < MAX_TRIES);//数据报文可能丢失，可重试5次

        socket.close();//关闭套接字
    }
}
