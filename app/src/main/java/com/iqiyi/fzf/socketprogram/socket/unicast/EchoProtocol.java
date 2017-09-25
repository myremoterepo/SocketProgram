package com.iqiyi.fzf.socketprogram.socket.unicast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by fzf on 17-9-22.
 */

public class EchoProtocol implements Runnable {
    private static final int BUF_SIZE = 32;
    private Socket clntSocket;
    private Logger logger;

    public EchoProtocol(Socket clntSocket, Logger logger){
        this.clntSocket = clntSocket;
        this.logger = logger;
    }

    public static void handleEchoClient(Socket clntSocket, Logger logger){
        try{
            InputStream in = clntSocket.getInputStream();
            OutputStream out = clntSocket.getOutputStream();

            int rcvMsgSize = 0;
            int totalBytesEcho = 0;
            byte[] echoBuf = new byte[BUF_SIZE];

            while((rcvMsgSize = in.read(echoBuf)) != -1){
                out.write(echoBuf, 0, rcvMsgSize);
                totalBytesEcho += rcvMsgSize;
            }

            logger.info("Client " + clntSocket.getRemoteSocketAddress() + " , echoed " + totalBytesEcho + " bytes");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                clntSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void run() {
        handleEchoClient(clntSocket, logger);
    }
}
