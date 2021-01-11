package com.demo.nettydemo.netty;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Objects;

import io.netty.buffer.ByteBuf;

/**
 * Created by ICE on 2021年1月11日.
 */
public class SocketUtils {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String transfer(@NonNull ByteBuf buf) {
        if (Objects.isNull(buf)) {
            return "";
        }
        String str;
        if (buf.hasArray()) {
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        }else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        return str;
    }


}
