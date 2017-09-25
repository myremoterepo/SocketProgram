package com.iqiyi.fzf.socketprogram;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iqiyi.fzf.socketprogram.channel.TCPEchoClientNonblocking;
import com.iqiyi.fzf.socketprogram.socket.tcp.TCPEchoClient;
import com.iqiyi.fzf.socketprogram.socket.tcp.TCPEchoServer;
import com.iqiyi.fzf.socketprogram.socket.udp.UDPEchoClient;
import com.iqiyi.fzf.socketprogram.socket.udp.UDPEchoServer;
import com.iqiyi.fzf.socketprogram.socket.unicast.ThreadExample;

import java.io.IOException;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testChannel();
    }

    /**
     * 测试信道
     * */
    public void testChannel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TCPEchoServer server = new TCPEchoServer();
                try {
                    server.doTask(20006);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                TCPEchoClientNonblocking channel = new TCPEchoClientNonblocking();
                try {
                    channel.doTask("127.0.0.1", 20006, "hello of channel");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public void testThread() {
        new Thread(new ThreadExample("hello0")).start();
        new Thread(new ThreadExample("hello1")).start();
        new Thread(new ThreadExample("hello2")).start();
    }

    private void testTCP() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TCPEchoServer server = new TCPEchoServer();
                try {
                    server.doTask(20006);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                TCPEchoClient client = new TCPEchoClient("a");
                try {
                    client.doTask("127.0.0.1", 20006, "hello");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                TCPEchoClient client = new TCPEchoClient("b");
                try {
                    client.doTask("127.0.0.1", 20006, "hello");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                TCPEchoClient client = new TCPEchoClient("c");
                try {
                    client.doTask("127.0.0.1", 20006, "hello");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                UDPEchoServer server = new UDPEchoServer();
                try {
                    server.doTask(20007);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                UDPEchoClient client = new UDPEchoClient();
                try {
                    client.doTask(InetAddress.getByName("127.0.0.1"), 20007, "hi");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
