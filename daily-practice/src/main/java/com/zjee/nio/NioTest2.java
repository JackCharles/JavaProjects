package com.zjee.nio;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class NioTest2 {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        SocketChannel socketChannel = serverSocketChannel.accept();

        ByteBuffer[] buffer = new ByteBuffer[3];
        buffer[0] = ByteBuffer.allocate(2);
        buffer[1] = ByteBuffer.allocate(4);
        buffer[2] = ByteBuffer.allocate(1024);
        while (true) {
            socketChannel.read(buffer);
            Arrays.stream(buffer).map(ByteBuffer::flip)
                    .forEach(b ->{
                        b.mark();
                        readBuffer(b);
                        b.reset();
                    });
            socketChannel.write(buffer);
            Arrays.stream(buffer).forEach(ByteBuffer::clear);
        }
    }

    private static void readBuffer(Buffer byteBuffer) {
    }
}
