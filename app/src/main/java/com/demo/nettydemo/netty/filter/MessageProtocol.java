package com.demo.nettydemo.netty.filter;





import androidx.annotation.NonNull;

import java.util.Arrays;

import lombok.Data;

/**
 * <pre>
 * 自己定义的协议
 *  数据包格式
 * +——----——+——-----——+——----——+
 * |协议开始标志|  长度             |   数据       |
 * +——----——+——-----——+——----——+
 * 1.协议开始标志head_data，为int类型的数据，16进制表示为0X76
 * 2.传输数据的长度contentLength，int类型
 * 3.要传输的数据
 * </pre>
 */
@Data
public class MessageProtocol {

    /**
     * 消息的开头信息标志
     */
    private int headData = ConstantValue.HEAD_DATA;

    /**
     * 消息的长度
     */
    private int contentLength;

    public int getHeadData() {
        return headData;
    }

    public byte[] getContent() {
        return content;
    }

    public int getContentLength() {
        return contentLength;
    }


    /**
     * 消息的内容
     */
    private byte[] content;

    public MessageProtocol(@NonNull byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageProtocol{" +
                "headData=" + headData +
                ", contentLength=" + contentLength +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
