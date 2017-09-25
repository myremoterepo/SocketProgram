package com.iqiyi.fzf.socketprogram.selector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by fzf on 17-9-25.
 */

public class EchoSelectorProtocol implements TCPProtocol {
    private final int bufSize;

    public EchoSelectorProtocol(int bufSize) {
        this.bufSize = bufSize;
    }

    @Override
    public void handleAccept(SelectionKey key) throws IOException {
        //从键中获取信道，接受连接
        //channel返回注册时创建的channel
        //accept返回一个SocketChannel实例
        SocketChannel clntChannel = ((ServerSocketChannel) key.channel()).accept();
        clntChannel.configureBlocking(false);
        //selector获取Selector
        //创建新的ByteBuffer实例，与register返回的SelectionKey实例关联
        clntChannel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
    }

    @Override
    public void handleRead(SelectionKey key) throws IOException {
        SocketChannel clntChannel = (SocketChannel) key.channel();//获取关联的信道，因为支持读写，所以是SocketChannel（？）
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();//获取关联的缓冲区
        long bytesRead = clntChannel.read(byteBuffer);//从信道读数据
        if (bytesRead == -1) {
            clntChannel.close();
        } else if (bytesRead > 0) {
            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);//标记为可读，可写
        }
    }

    @Override
    public void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();//获取数据缓冲区
        byteBuffer.flip();//使写数据开始消耗由读操作产生的数据
        SocketChannel clntChannel = (SocketChannel) key.channel();//获取信道
        clntChannel.write(byteBuffer);
        if (byteBuffer.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);//缓冲区还有剩余，标记为可读
        }
        byteBuffer.compact();//压缩缓冲区
    }
}
