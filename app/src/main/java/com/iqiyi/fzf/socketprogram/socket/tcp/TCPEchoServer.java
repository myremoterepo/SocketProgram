package com.iqiyi.fzf.socketprogram.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by fzf on 17-9-21.
 */

public class TCPEchoServer {
    private static final int BUF_SIZE = 32;

    public void doTask(int serverPort) throws IOException {
        ServerSocket serverSocket = new ServerSocket();//ServerSocket为新的TCP连接提供新的已连接的Socket实例
        serverSocket.setReuseAddress(true);//当一个连接关闭，通信的一端或者两端必须对传输中丢失的数据包进行清理，需要能够与正在使用的地址进行绑定的功能，因此，开启地址重用
        serverSocket.bind(new InetSocketAddress(serverPort));

        int rcvMsgSize = 0;//读取的字节数，存储read的返回值
        byte[] rcvBuf = new byte[BUF_SIZE];

        while(true){
            //accept阻塞等待，等待指向端口的连接，返回一个已连接的Socket实例
            //对于ServerSocket创建但未调用accept方法的情况，新的连接会排在一个队列中，此时调用accept会立即响应
            Socket clntSocket = serverSocket.accept();
            SocketAddress clientAddress = clntSocket.getRemoteSocketAddress();
            System.out.println("handling client at " + clientAddress.toString());

            InputStream in = clntSocket.getInputStream();
            OutputStream out = clntSocket.getOutputStream();

            //循环将数据流读到rcvBuf字节数组中
            //read阻塞等待，当客户端关闭连接时会读取到-1
            //write将收到的字节原样发给客户端以形成echo
            while ((rcvMsgSize = in.read(rcvBuf)) != -1){
                out.write(rcvBuf, 0, rcvMsgSize);
            }

            clntSocket.close();//关闭套接字
        }
    }
}
