package com.iqiyi.fzf.socketprogram.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * Created by fzf on 17-9-25.
 */

public class UDPEchoServerSelector {
    private static final int BUF_SIZE = 255;
    private static final int TIME_OUT = 3000;

    public void doTaks(int port) throws IOException {
        Selector selector = Selector.open();

        DatagramChannel datagramChannel = DatagramChannel.open();

        datagramChannel.configureBlocking(false);
        datagramChannel.socket().bind(new InetSocketAddress(port));
        datagramChannel.register(selector, SelectionKey.OP_READ);

        while (true) {
            if (selector.select(TIME_OUT) == 0){
                System.out.println(".");
                continue;
            }

            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();

            while (selectionKeyIterator.hasNext()){
                SelectionKey selectionKey = selectionKeyIterator.next();

                if (selectionKey.isReadable()){
                    handleRead(selectionKey);
                }

                if (selectionKey.isValid() && selectionKey.isWritable()){
                    handleWrite(selectionKey);
                }

                selectionKeyIterator.remove();
            }
        }
    }

    private void handleWrite(SelectionKey selectionKey) throws IOException {
        DatagramChannel channel = (DatagramChannel) selectionKey.channel();

        ClientRecord clientRecord = (ClientRecord) selectionKey.attachment();
        clientRecord.buffer.flip();

        int byteSend = channel.send(clientRecord.buffer, clientRecord.clientAddress);

        if (byteSend != 0){
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }

    private void handleRead(SelectionKey selectionKey) throws IOException {
        DatagramChannel channel = (DatagramChannel) selectionKey.channel();

        ClientRecord clientRecord = (ClientRecord) selectionKey.attachment();

        clientRecord.buffer.clear();
        clientRecord.clientAddress = channel.receive(clientRecord.buffer);

        if (clientRecord.clientAddress != null){
            selectionKey.interestOps(SelectionKey.OP_WRITE);

        }
    }


    static class ClientRecord{
        public SocketAddress clientAddress;
        public ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
    }

}
