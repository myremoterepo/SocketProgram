package com.iqiyi.fzf.socketprogram.selector;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by fzf on 17-9-25.
 */

public interface TCPProtocol {
    public void handleAccept(SelectionKey key) throws IOException;

    public void handleRead(SelectionKey key) throws IOException;

    public void handleWrite(SelectionKey key) throws IOException;
}
