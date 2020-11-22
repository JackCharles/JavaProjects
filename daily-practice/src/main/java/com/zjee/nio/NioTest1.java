package com.zjee.nio;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NioTest1 {
    public static void main(String[] args) throws Exception {
        FileChannel fileChannel = FileChannel.open(Paths.get("TestNio.txt"));
        fileChannel.transferTo(0, Integer.MAX_VALUE,
                FileChannel.open(Paths.get("TestNioCpy.txt"),
                        StandardOpenOption.WRITE, StandardOpenOption.CREATE));

//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//        byteBuffer.put(("参差荇菜，左右采之。窈窕淑女，琴瑟友之。\n" +
//                "参差荇菜，左右芼之。窈窕淑女，钟鼓乐之。").getBytes());
//        byteBuffer.flip();
//        fileChannel.write(byteBuffer);
        fileChannel.close();
        ByteBuffer buffer = ByteBuffer.allocateDirect(100);
    }
}
