package com.iqiyi.fzf.socketprogram.socket.unicast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by fzf on 17-9-22.
 * 一客户一线程
 */

public class TCPEchoServerThread {

    public void doTask(int echoServerPort) throws IOException {
        ServerSocket socket = new ServerSocket(echoServerPort);
        Logger logger = Logger.getLogger("practical");

        while (true){
            Socket clntSocket = socket.accept();
            Thread thread = new Thread(new EchoProtocol(clntSocket, logger));
            thread.start();

            logger.info("created and started thread " + thread.getName());
        }
    }
}
