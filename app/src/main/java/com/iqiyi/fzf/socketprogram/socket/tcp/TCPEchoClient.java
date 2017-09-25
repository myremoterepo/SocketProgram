package com.iqiyi.fzf.socketprogram.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by fzf on 17-9-21.
 * UDP尽可能地传送消息，但并不一定保证信息一定成功到达目的地，且信息到达的顺序与发送顺序不一样
 *
 * UDP效率高，比TCP少连接的建立过程；灵活性，最小开销满足任何需求（？说的毛）
 *
 *
 * TCP协议约束：信息必须在块中接收和发送，块的长度必须时8位。可把传输的信息看作数组，每个数字的取值范围是0-255（与8位二进制相对应）。因此TCP可以发送和接收数组，每个数组元素是范围在0-255之间的整数
 *
 */

public class TCPEchoClient {

    private String mClientName;
    public TCPEchoClient(String name){
        mClientName = name;
    }
    public void doTask(String serverAddress, int serverPort, String message) throws IOException {
        byte[] data = message.getBytes();//获取字节数组
        Socket socket = new Socket(serverAddress, serverPort);//创建流套接字，并且连接到特定的端口和主机
        System.out.println("connecting to server.." + serverAddress + "..sending echo string");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        out.write(data);//将字节写入OutputStream来发送数据，OutputStream将字节数据通过之前建立的连接发送到指定的服务器

        int totalBytesRcvd = 0;
        int bytesRcvd;

        //重复执行接收过程。字符串可能位于服务器的多个块中，TCP也可能会将字符串分割成多个部分，因此用一个read是错误的。
        //可在此理解TCP不保留边界信息。
        while(totalBytesRcvd < data.length){
            //通过InputStream接受数据。
            //read在没有可读数据时会阻塞等待，循环将数据读到data字节数组。
            //如果TCP连接被另一端关闭，read会返回-1，表示服务器关闭了套接字。
            if ((bytesRcvd = in.read(data, totalBytesRcvd, data.length - totalBytesRcvd)) == -1){
                throw new SocketException("connection closed premature");
            }

            totalBytesRcvd += bytesRcvd;
        }

        System.out.println("Received: " + new String(data) + " at client " + mClientName);//将字节数组转换成字符串

        socket.close();//关闭套接字
    }
}
