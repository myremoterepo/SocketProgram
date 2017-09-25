package com.iqiyi.fzf.socketprogram.socket.unicast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by fzf on 17-9-25.
 */

public class TCPEchoSerExector {

    public void doTask(int serverPort) throws IOException {
        ServerSocket serverSocket = new ServerSocket(serverPort);
        Logger logger = Logger.getLogger("practical");
        Executor service = Executors.newCachedThreadPool();

        while (true) {
            Socket clntSocket = serverSocket.accept();
            service.execute(new EchoProtocol(clntSocket, logger));
        }
    }
}
