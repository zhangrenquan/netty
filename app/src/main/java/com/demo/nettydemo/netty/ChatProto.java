package com.demo.nettydemo.netty;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.demo.nettydemo.netty.filter.MessageProtocol;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;


/**
 * Created by ICE on 2019-06-10.
 */
public class ChatProto {

    //header
    public static final int PING_PROTO  = 1 << 8 | 220; //ping消息
    public static final int PONG_PROTO  = 2 << 8 | 220; //pong消息
    public static final int SYS_PROTO   = 3 << 8 | 220; //系统消息
    public static final int ERROR_PROTO = 4 << 8 | 220; //错误消息
    public static final int AUTH_PROTO  = 5 << 8 | 220; //认证消息
    public static final int MSG_PROTO   = 6 << 8 | 220; //普通消息

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getBody() {
        return timestamp;
    }

    public void setBody(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    private int head;
    private String timestamp;
    private Map<String, Object> extend;

    public ChatProto(int head, String timestamp) {
        this.head = head;
        this.timestamp = timestamp;
        this.extend = new HashMap<>();
    }

    public static MessageProtocol buildPingProto() {
//        Log.i("TAG","ping");
        return buildProto(PING_PROTO);
    }

    public static MessageProtocol buildMsgProto(@NonNull Integer code) {
        ChatProto chatProto = new ChatProto(MSG_PROTO, null);
        Log.i("TAG", JSONObject.toJSONString(chatProto)+"");
        return buildProto(MSG_PROTO, code, null);
    }

    public static MessageProtocol buildPongProto() {
        return buildProto(PONG_PROTO);
    }

    public static MessageProtocol buildSysProto(int code, @NonNull String msg) {
        return buildProto(SYS_PROTO, code, msg);
    }

    public static MessageProtocol buildErrProto(int code, @NonNull String msg) {
        return buildProto(ERROR_PROTO, code, msg);
    }





    public static MessageProtocol buildMsgProto(@NonNull Integer code, @NonNull Long apkId) {
        ChatProto chatProto = new ChatProto(MSG_PROTO, null);
        chatProto.extend.put("code", code);
        chatProto.extend.put("apkId", apkId);
        chatProto.extend.put("time", System.currentTimeMillis());
        return new MessageProtocol(JSONObject.toJSONString(chatProto).getBytes());
    }

    public static MessageProtocol buildMsgProto(@NonNull Integer code, @Nullable Integer count) {
        ChatProto chatProto = new ChatProto(MSG_PROTO, null);
        chatProto.extend.put("code", code);
        if (null != count) {
            chatProto.extend.put("count", count);
        }
        chatProto.extend.put("time", System.currentTimeMillis());
        return new MessageProtocol(JSONObject.toJSONString(chatProto).getBytes());
    }

    public static MessageProtocol buildAuthProto(@Nullable @NonNull String token, @Nullable @NonNull Long giftId, Object o) {
        ChatProto chatProto = new ChatProto(AUTH_PROTO, null);
//        if (null != token) {
//            chatProto.extend.put("nickName", token);
//        }
//        if (null != giftId) {
//            chatProto.extend.put("giftId", giftId);
//        }
//        chatProto.extend.put("time", System.currentTimeMillis());
        chatProto.extend.put("发送数据","发送数据");

        return new MessageProtocol(JSONObject.toJSONString(chatProto).getBytes());
    }

    public static MessageProtocol buildMsgProto(@Nullable Long giftId, @NonNull String nickName, @Nullable @NonNull Integer level, String msg) {
        ChatProto chatProto = new ChatProto(MSG_PROTO, null);
        if (null != level) {
            chatProto.extend.put("level", level);
        }
        if (!nickName.isEmpty()) {
            chatProto.extend.put("nickName", nickName);
        }
        if (null != giftId) {
            chatProto.extend.put("giftId", giftId);
        }
        chatProto.extend.put("time", System.currentTimeMillis());
        return new MessageProtocol(JSONObject.toJSONString(chatProto).getBytes());
    }


    public static MessageProtocol buildMsgProto(int code, @NonNull String nickName, @Nullable Integer level, @Nullable Long giftId, @Nullable Integer count, String msg) {
        ChatProto chatProto = new ChatProto(MSG_PROTO, msg);
        chatProto.extend.put("code", code);
        if (!nickName.isEmpty()) {
            chatProto.extend.put("nickName", nickName);
        }
        if (null != level) {
            chatProto.extend.put("level", level);
        }
        if (null != giftId) {
            chatProto.extend.put("giftId", giftId);
        }
        if (null != count) {
            chatProto.extend.put("count", count);
        }
        chatProto.extend.put("time", System.currentTimeMillis());
        return new MessageProtocol(JSONObject.toJSONString(chatProto).getBytes());
    }

    private static MessageProtocol buildProto(int head) {
        ChatProto chatProto = new ChatProto(head, null);
        Log.i("TAG","发送数据-------JSONObject    "+ JSONObject.toJSONString(chatProto));
        return new MessageProtocol(JSONObject.toJSONString(chatProto).getBytes());
    }

    @NonNull
    private static MessageProtocol buildProto(int head, int code, @NonNull String msg) {
        ChatProto chatProto = new ChatProto(head, null);
        chatProto.extend.put("code", code);
        chatProto.extend.put("msg", msg);
        String bytes = JSONObject.toJSONString(chatProto);
        MessageProtocol messageProtocol = new MessageProtocol(JSONObject.toJSONString(chatProto).getBytes());
        Log.i("TAG","发送数据-------    "+messageProtocol);
        return new MessageProtocol(JSONObject.toJSONString(chatProto).getBytes());
    }

    public static void main(String[] args){
        ChatProto chatProto = new ChatProto(AUTH_PROTO, "哈喽，world！！！");
        chatProto.extend.put("userId", 2L);
        chatProto.extend.put("userName", "n_7777777");
        chatProto.extend.put("level", 1);
        chatProto.extend.put("time", System.currentTimeMillis());
        String message = JSONObject.toJSONString(chatProto);


        System.out.println(">>>>>" + message);
        System.out.println("======" + Unpooled.copiedBuffer(message.getBytes()).toString(CharsetUtil.UTF_8));

        ChatProto chatProto1 = new ChatProto(PING_PROTO, null);

        String message1 = JSONObject.toJSONString(chatProto1);
        System.out.println(">>>>>" + message1);

        try {
            String enStr = URLEncoder.encode(message, "UTF-8");
            System.out.println(">>>>>" + enStr);

            String enStr1 = URLEncoder.encode(message1, "UTF-8");
            System.out.println(">>>>>" + enStr1);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
