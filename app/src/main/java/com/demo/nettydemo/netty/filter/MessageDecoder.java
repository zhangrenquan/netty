package com.demo.nettydemo.netty.filter;






import androidx.annotation.NonNull;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Created by ICE on 2021年1月11日.
 */
public class MessageDecoder extends ByteToMessageDecoder {

    /**
     * <pre>
     * 协议开始的标准head_data，int类型，占据4个字节.
     * 表示数据的长度contentLength，int类型，占据4个字节.
     * </pre>
     */
    public static final int BASE_LENGTH = 4 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, @NonNull ByteBuf byteBuf, @NonNull List<Object> list) throws Exception {
        //可读长度大于基本长度
        if (byteBuf.readableBytes() > BASE_LENGTH) {
            //防止socket字节流攻击
            //防止客户端传来的数据过大
            if (byteBuf.readableBytes() > 2048) {
                byteBuf.skipBytes(byteBuf.readableBytes());
            }

            //记录包头开始的index
            int beginReader;

            while (true) {
                //获取包头开始的index
                beginReader = byteBuf.readerIndex();
                //标记包头开始的index
                byteBuf.markReaderIndex();
                //读到了协议的开始标志，结束while循环
                if (byteBuf.readInt() == ConstantValue.HEAD_DATA) {
                    break;
                }

                //未读到包头，略过一个字符，继续读取包头的开始标记信息
                byteBuf.resetReaderIndex();
                byteBuf.readByte();

                //当略过一个字符后，数据包长度不符合，等待后续数据到达
                if (byteBuf.readableBytes() < BASE_LENGTH) {
                    return;
                }
            }

            //消息的长度
            int length = byteBuf.readInt();
            //判断数据包是否完整
            if (byteBuf.readableBytes() < length) {
                //还原读指针
                byteBuf.readerIndex(beginReader);
                return;
            }

            //读取消息内容
            byte[] data = new byte[length];
            byteBuf.readBytes(data);

            MessageProtocol protocol = new MessageProtocol(data);
            list.add(protocol);
        }
    }

}
