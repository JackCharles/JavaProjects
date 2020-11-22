package com.zjee.netty;

import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

    static Gson gson = new Gson();

    static SslContext sslContext;

    static {
        try {
            sslContext = SslContextBuilder.forClient().build();
        } catch (SSLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ChannelFuture future = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress("zjee.ml", 443))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addFirst(new SslHandler(sslContext.newEngine(ch.alloc())))
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
                                .addLast(new SimpleChannelInboundHandler<HttpObject>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
                                        if (msg instanceof FullHttpResponse) {
                                            FullHttpResponse response = (FullHttpResponse) msg;
                                            response.headers().forEach(System.out::println);
                                            System.out.println(response.content().toString(CharsetUtil.UTF_8));
                                        }
                                    }

                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        ctx.writeAndFlush(new DefaultHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/"));
                                    }
                                });
                    }
                }).connect().sync();
        future.channel().closeFuture().sync();
    }
}

class ClientHandler extends SimpleChannelInboundHandler<String> {
    Gson gson = new Gson();
    Scanner scanner = new Scanner(System.in);
    String user = "";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Map map = gson.fromJson(msg, Map.class);
        String opt = (String) map.getOrDefault("opt", "unknown");
        if ("login".equals(opt)) {
            System.out.print(map.get("userName") + " 登录成功, 你的在线好友：" + map.get("friends"));
        } else if ("send".equals(opt)) {
            System.out.print("【" + map.get("from") + "】说：" + map.get("msg"));
        } else {
            System.out.print(msg);
        }

        new Thread(() -> {
            while (true) {
                System.out.println("请输入你的消息(msg receiver)：");
                String input = scanner.nextLine();
                String[] s = input.split("@");
                if (s.length < 2) {
                    System.out.println("输入不合法，请重试!");
                    continue;
                }
                Map tmap = new HashMap();
                tmap.put("opt", "send");
                tmap.put("from", user);
                tmap.put("to", s[1].trim());
                tmap.put("msg", s[0].trim());
                ctx.writeAndFlush(gson.toJson(tmap));
            }
        }).start();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Map<String, Object> req = new HashMap<>();
        req.put("opt", "login");
        System.out.print("请输入用户名: ");
        user = scanner.next();
        req.put("userName", user);
        System.out.println("登陆中,请等待...");
        ChannelPromise channelPromise = ctx.newPromise();
        ctx.channel().writeAndFlush(gson.toJson(req), channelPromise);
        if (!channelPromise.isSuccess()) {
            channelPromise.cause().printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}