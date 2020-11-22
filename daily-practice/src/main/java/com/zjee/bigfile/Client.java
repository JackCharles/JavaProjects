package com.zjee.bigfile;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GenericProgressiveFutureListener;

import java.io.File;
import java.net.InetSocketAddress;

public class Client {
    public static void main(String[] args) throws Exception {

        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("localhost", 8080))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            ChannelHandlerContext context;
                            File file;

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                if(((ByteBuf)msg).toString(CharsetUtil.UTF_8).equals("OK")) {
                                  doSendFile();
                                }
                            }

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                this.context = ctx;
                                sendFile("D:\\Movies\\V字仇杀队.mkv");
                            }


                            public void sendFile(String fileName) {
                                this.file = new File(fileName);
                                context.writeAndFlush(Unpooled.copyLong(file.length()));
                            }


                            private void doSendFile() {
                                DefaultFileRegion region = new DefaultFileRegion(file, 0, file.length());
                                ChannelProgressivePromise promise = new DefaultChannelProgressivePromise(context.channel());
                                context.writeAndFlush(region, promise);
                                promise.addListener(new GenericProgressiveFutureListener<ChannelProgressiveFuture>() {
                                    @Override
                                    public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                                        if (!future.isSuccess()) {
                                            future.cause().printStackTrace();
                                        } else {
                                            System.out.println("done");
                                            file = null;
                                        }
                                    }

                                    @Override
                                    public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                                        System.out.println(String.format("progress: %.2f%%", progress * 100.0 / total));
                                    }
                                });
                            }
                        });
                    }
                })
                .connect().sync();
        channelFuture.channel().closeFuture().sync();
    }
}
