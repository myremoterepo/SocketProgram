package com.iqiyi.fzf.socketprogram.socket.unicast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by fzf on 17-9-25.
 */

public class TimeLimitEchoProtocol implements Runnable {
    private static final int BUF_SIZE = 32;
    private static final String TIME_LIMIT = "10000";
    private static final String TIME_LIMIT_PROP = "TimeProp";
    private Socket clntSocket;
    private Logger logger;
    private int timeLimit;

    public TimeLimitEchoProtocol(Socket clntSocket, Logger logger) {
        this.clntSocket = clntSocket;
        this.logger = logger;
        timeLimit = Integer.parseInt(TIME_LIMIT);
    }

    private void handleEchoClient(Socket clntSocket, Logger logger) {
        try {
            InputStream in = clntSocket.getInputStream();
            OutputStream out = clntSocket.getOutputStream();

            int rcvMsgSize = 0;
            int totalBytesEchod = 0;
            byte[] echoBuf = new byte[BUF_SIZE];
            long endTime = System.currentTimeMillis() + timeLimit;
            int timeBoundMillis = timeLimit;

            clntSocket.setSoTimeout(timeBoundMillis);

            while (timeBoundMillis > 0 && (rcvMsgSize = in.read(echoBuf)) != -1) {
                out.write(echoBuf, 0, rcvMsgSize);
                totalBytesEchod += rcvMsgSize;
                timeBoundMillis = (int) (endTime - System.currentTimeMillis());
                clntSocket.setSoTimeout(timeBoundMillis);
            }
            logger.info("client " + clntSocket.getRemoteSocketAddress() + " , echoed " + totalBytesEchod + " bytes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        handleEchoClient(clntSocket, logger);
    }
}
