package com.demo.nettydemo.netty;

import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.demo.nettydemo.netty.filter.MessageProtocol;


import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by ICE on 2021年1月11日.
 */
@Slf4j
@ChannelHandler.Sharable
public class SocketClientHandler extends ChannelInboundHandlerAdapter {

    private SocketClient client;

    public SocketClientHandler(SocketClient client) {
        this.client = client;
    }


    /**
     * 收到消息
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, @Nullable Object msg) {
        Log.i("TAG", "channelRead");
        if (null == msg) return;
        if (!(msg instanceof MessageProtocol)) return;
        MessageProtocol message = (MessageProtocol) msg;
        String body = new String(message.getContent());
        Log.i("TAG", "【Socket】客户端收到消息：{}" + body);
        JSONObject json = JSONObject.parseObject(body);
        if (null == json) return;
        //pong
        if (json.getInteger("head") == ChatProto.PONG_PROTO) {
            Log.i("TAG", "【Socket】服务端返回pong消息" + json.toString());
        }
        //普通消息
        if (json.getInteger("head") == ChatProto.MSG_PROTO) {
            Log.i("TAG", "收到端消息：{}" + body);
        }
//            client.getListener().listen(body);

    }

    @Override
    public void channelActive(@NonNull ChannelHandlerContext ctx) {
        //发送一次授权
        ctx.writeAndFlush(ChatProto.buildAuthProto(client.getToken(), 1L, null));
//        ctx.channel().read();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Log.i("TAG", "【socket】 SocketClientHandler 客户端终止---正在重新连接");
        client.connect();
        super.channelInactive(ctx);
    }


    @Override
    public void userEventTriggered(@NonNull ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                Log.i("TAG", "长期没收到服务器推送数据");
                //可以选择重新连接
//                client.connect();
            }

            if (event.state().equals(IdleState.WRITER_IDLE)) {
//                Log.i("TAG","长期没发送信息到服务器服务器");
                ctx.writeAndFlush(ChatProto.buildPingProto());
            }
        }
        ctx.fireUserEventTriggered(evt);
    }
}
