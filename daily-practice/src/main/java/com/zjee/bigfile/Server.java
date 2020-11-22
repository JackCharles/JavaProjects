package com.zjee.bigfile;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Server {
    public static void main(String[] args) {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                FileChannel fileChannel;
                                long fileLen = -1;
                                long fileRead = 0;

                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                    if(fileLen < 0) {
                                        fileLen = msg.readLong();
                                        ctx.writeAndFlush(Unpooled.copiedBuffer("OK", CharsetUtil.UTF_8));
                                    }

                                    else {
                                        if (fileChannel == null) {
                                            fileChannel = FileChannel.open(Paths.get("C:\\Users\\zhongjie\\Desktop\\copy.mkv"),
                                                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                                        }
                                        int readableBytes = msg.readableBytes();
                                        ByteBuffer buffer = msg.nioBuffer();
                                        fileChannel.write(buffer);
                                        fileRead += readableBytes;
                                        if(fileRead >= fileLen) {
                                            fileLen = -1;
                                            fileRead = 0;
                                            fileChannel.close();
                                            System.out.println("read complete");
                                        }
                                    }
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            });
                        }
                    }).bind(8080)
                    .sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
