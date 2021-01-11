package com.demo.nettydemo.netty.filter;






import androidx.annotation.NonNull;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by ICE on 2021年1月11日.
 */
public class MessageEncoder extends MessageToByteEncoder<MessageProtocol> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, @NonNull MessageProtocol messageProtocol, @NonNull ByteBuf byteBuf) throws Exception {
        //写入消息
        //1.写入消息的开头信息标志（int）
        byteBuf.writeInt(messageProtocol.getHeadData());
        //2.写入消息的长度（int）
        byteBuf.writeInt(messageProtocol.getContentLength());
        //3.写入消息的内容（byte[]）
        byteBuf.writeBytes(messageProtocol.getContent());
    }
}
