package com.zjee.netty;

import com.google.gson.Gson;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup =  new NioEventLoopGroup();
        EventLoopGroup workerGroup =  new NioEventLoopGroup();

        ChannelFuture channelFuture = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                                .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                .addLast(new StringEncoder(CharsetUtil.UTF_8))
                                .addLast(ServerHandler.getInstance());
                    }
                }).bind(9000).await().sync();
    }
}


@ChannelHandler.Sharable
class ServerHandler extends SimpleChannelInboundHandler<String> {
     ConcurrentHashMap<ChannelHandlerContext, String> clients = new ConcurrentHashMap<>();
     ConcurrentHashMap<String, ChannelHandlerContext> users = new ConcurrentHashMap<>();
    static Gson gson = new Gson();

    static ServerHandler handler = null;

    public static ServerHandler getInstance() {
        if(handler == null) {
            handler = new ServerHandler();
        }
        return handler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("==== Server: " + msg);
        Map map = gson.fromJson(msg, Map.class);
        String opt = (String) map.getOrDefault("opt", "unknown");
        if("login".equals(opt)){
            String user = (String) map.get("userName");
            System.out.println(user + "上线了, 地址：" + ctx.channel().remoteAddress());
            clients.put(ctx, user);
            users.put(user, ctx);
            map.put("code", 0);
            map.put("msg", "ok");
            map.put("friends", users.keySet());
            ctx.writeAndFlush(gson.toJson(map));
        }
        else if("send".equals(opt)){
            String to = (String) map.get("to");
            if(users.containsKey(to)) {
                map.remove("to");
                ChannelHandlerContext context = users.get(to);
                ChannelPromise channelPromise = context.newPromise();
                context.writeAndFlush(gson.toJson(map), channelPromise);
                if(!channelPromise.isSuccess()) {
                    channelPromise.cause().printStackTrace();
                }
            }else {
                map = new HashMap();
                map.put("opt", "feedback");
                map.put("msg", to + "不在线, 请稍后再试");
                ctx.writeAndFlush(gson.toJson(map));
            }
        }else if("quit".equals(opt)) {

        }else {

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        String user = clients.remove(ctx);
        if(user != null) {
            System.out.println(user + "掉线了...");
            users.remove(user);
        }
    }
}