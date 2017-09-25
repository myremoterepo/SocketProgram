package com.iqiyi.fzf.socketprogram.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by fzf on 17-9-25.
 */

public class TCPEchoClientNonblocking {

    public void doTask(String serverAddr, int serverPort, String msg) throws IOException {
        SocketChannel clntChannel = SocketChannel.open();//创建非阻塞式的SocketChannel
        clntChannel.configureBlocking(false);

        //连接服务器，非阻塞的原因，connect会立即返回，有两种结果：连接建立后返回true，连接建立前返回false
        //连接建立前发送或者接收数据都会产生NotYetConnectionException
        if (!clntChannel.connect(new InetSocketAddress(serverAddr, serverPort))){
            while (!clntChannel.finishConnect()){//轮询连接状态，等待连接建立的过程中可以执行其他的任务
                System.out.print(".");
            }
        }

        ByteBuffer writeBuffer = ByteBuffer.wrap(msg.getBytes());//创建写缓冲区，包含要发送的字节数组
        ByteBuffer readBuffer = ByteBuffer.allocate(msg.length());//创建读缓冲区，allocate

        int totalBytesRcvd = 0;
        int bytesRcvd = 0;

        //循环知道发送和接收完所有的数据
        while (totalBytesRcvd < msg.length()){
            if (writeBuffer.hasRemaining()){//只要输出缓冲区还有数据就write
                clntChannel.write(writeBuffer);
            }

            if ((bytesRcvd = clntChannel.read(readBuffer)) == -1){//read不会等待，当没有数据可读返回0
                throw new SocketException("Connection closed premature");
            }
            totalBytesRcvd += bytesRcvd;
            System.out.println("*");
        }

        System.out.println("received " + new String(readBuffer.array(), 0, totalBytesRcvd));

        clntChannel.close();//关闭信道
    }
}
