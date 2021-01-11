package com.demo.nettydemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.nettydemo.netty.SocketClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button run_netty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        run_netty = (Button) findViewById(R.id.run_netty);
        run_netty.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.run_netty:
                // TODO 21/01/11
                //netty长连接进行连接
                SocketClient socketClient = new SocketClient();
//                socketClient.run("host",port);
                break;
            default:
                break;
        }
    }
}