package com.demo.nettydemo.netty;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.demo.nettydemo.netty.filter.MessageDecoder;
import com.demo.nettydemo.netty.filter.MessageEncoder;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ICE on 2019-06-10.
 */
@Slf4j
public class SocketClient implements Start {
    private Channel channel;
    private final Bootstrap bs = new Bootstrap();
    private final EventLoopGroup group = new NioEventLoopGroup();
    private final SocketClientHandler handler = new SocketClientHandler(this);

    private String host;
    private int port;

    private MessageListener listener;
    private String token;

    public MessageListener getListener() {
        return listener;
    }

    public String getToken() {
        return token;
    }

    public void addListener(MessageListener listener) {
        this.listener = listener;
    }

    public SocketClient() {
        bs.group(group)
                .channel(NioSocketChannel.class)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(@NonNull SocketChannel socketChannel) throws Exception {
                        ChannelPipeline p = socketChannel.pipeline();
                        //定时器
                        p.addLast(new IdleStateHandler(40, 30, 0, TimeUnit.SECONDS));

                        //编码器 解码器
//                        p.addLast("encoder", new StringDecoder(StandardCharsets.UTF_8));
//                        p.addLast("decoder", new StringDecoder(StandardCharsets.UTF_8));
                        p.addLast(new MessageDecoder());
                        p.addLast(new MessageEncoder());
                        p.addLast(handler);

                    }
                });
    }

    @Override
    public void run(String host, int port) {
        this.host = host;
        this.port = port;

        if (null != channel && channel.isActive()) {
            return;
        }

        connect();
    }


    public void connect() {
        try {
            ChannelFuture f = bs.connect(new InetSocketAddress(host, port)).sync();
//            ChannelFuture f = bs.connect(host, port);
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        channel = channelFuture.channel();
                        Log.i("TAG", "【socket】客户端启动成功，START SUCCESS！！！");
                        channelFuture.channel().writeAndFlush(ChatProto.buildPingProto());
                    }
                }
            });

            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            Log.i("TAG", "【socket】客户端启动异常, {}" + e.getMessage());
        } finally {
            try {
                group.shutdownGracefully().sync();
                group.shutdownNow();
                Log.i("TAG", "【socket】客户端断开连接，重新连接");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            try {
//
////                group.shutdownGracefully().sync();
////                group.shutdownNow();
//
//                TimeUnit.SECONDS.sleep(5);
//                Log.i("TAG", "finally reconnect");
//                connect();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }


//
//        try {
////            ChannelFuture f = bs.connect(host, port).sync();
//            ChannelFuture f = bs.connect(new InetSocketAddress(host, port)).sync();
////            ChannelFuture f = bs.connect(new InetSocketAddress("dev.so.client.facekeji.com", 9499)).sync();
//
//            f.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(@NonNull ChannelFuture channelFuture) throws Exception {
//                    if (channelFuture.isSuccess()) {
//                        MyApp.isConnectStop = false;
//                        MyApp.isLast = true;
//                        channel = channelFuture.channel();
//                        Log.i("TAG", "【socket】客户端启动成功，START SUCCESS！！！");
//                        channel.writeAndFlush(ChatProto.buildMsgProto(ChatProto.MSG_PROTO));
////                        channel.writeAndFlush(ChatProto.buildPingProto());
////                        channel.writeAndFlush(ChatProto.buildAuthProto(getToken(), 1L, null));
//                    } else {
//                        channelFuture.channel().eventLoop().schedule(new Runnable() {
//                            @Override
//                            public void run() {
//                                MyApp.isConnectStop = true;
//                                Log.i("TAG", "【socket】客户端启动失败重连");
////                                connect();
//                            }
//                        }, 10, TimeUnit.SECONDS);
//                    }
//                }
//            });
//
//            // 连接后成功后再阻塞主线程
//            // 等待客户端链路关闭
//            // 当客户端连接关闭之后，客户端主函数退出.
//            f.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            MyApp.isConnectStop = true;
//            Log.i("TAG", "【socket】客户端启动异常, {}" + e.getMessage());
//        } finally {
//            Log.i("TAG", "【socket】客户端终止");
//            group.shutdownGracefully();
//
//        }
    }

    public void stop() {
        Log.i("TAG", "【socket】主动客户端终止");
        group.shutdownGracefully();
    }

    public void authToken(String token) {
        if (null != token && token.trim().length() > 0) {
            this.token = token;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendMsg(Long userId, String userName, Integer level, String message) {
        if (Objects.nonNull(channel)) {
            channel.writeAndFlush(ChatProto.buildMsgProto(userId, userName, level, message));
        }
    }
}
