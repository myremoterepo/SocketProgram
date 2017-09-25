package com.iqiyi.fzf.socketprogram.socket.unicast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by fzf on 17-9-25.
 */

public class TCPEchoServerPool {
    public void doTask(int port, int threadPoolSize) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);

        final Logger logger = Logger.getLogger("practical");

        for (int i = 0; i < threadPoolSize; i++){
            Thread thread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (true){
                        try {
                            Socket clntSocket = serverSocket.accept();

                            EchoProtocol.handleEchoClient(clntSocket, logger);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
            logger.info("created and started thread " + thread.getName());
        }
    }
}
