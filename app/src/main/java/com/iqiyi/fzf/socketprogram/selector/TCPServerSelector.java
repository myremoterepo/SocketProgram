package com.iqiyi.fzf.socketprogram.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * Created by fzf on 17-9-25.
 */

public class TCPServerSelector {
    private static final int BUF_SIZE = 255;
    private static final int TIME_OUT = 3000;

    public void doTask(int[] ports) throws IOException {
        Selector selector = Selector.open();

        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel listnChannel = ServerSocketChannel.open();//创建ServerSocketChannel实例
            listnChannel.socket().bind(new InetSocketAddress(ports[i]));//监听指定端口
            listnChannel.configureBlocking(false);//非阻塞模式，非阻塞模式才可以注册选择器
            listnChannel.register(selector, SelectionKey.OP_ACCEPT);//注册选择器，并指出可accept
        }

        TCPProtocol protocol = new EchoSelectorProtocol(BUF_SIZE);//协议操作器

        while (true) {//等待I/O，调用操作器
            if (selector.select(TIME_OUT) == 0) {//select阻塞等待，知道有准备好的信道或者超时
                System.out.print(".");
                continue;
            }

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();//获取键集，包含准备好的某一信道的SelectionKey
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {//接收就绪
                    protocol.handleAccept(key);
                }

                if (key.isReadable()) {//读就绪
                    protocol.handleRead(key);
                }

                if (key.isValid() && key.isWritable()) {//写就绪
                    protocol.handleWrite(key);
                }

                keyIterator.remove();//移除键
            }
        }
    }
}
