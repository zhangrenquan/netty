# netty
netty长连接
github：https://github.com/zhangrenquan/netty.git

准备工作：
    依赖：
            implementation 'io.netty:netty-all:4.1.36.Final'
            compileOnly "org.projectlombok:lombok:1.16.18"
            implementation 'org.glassfish:javax.annotation:10.0-b28'
    导入jar包：
            libs-->fastjson-1.2.49.jar
    
    ChatProto自定义消息格式与消息文体
    MessageEncoder MessageDecoder 消息加密处理类
    SocketUtils文本消息转换类
    SocketClient启动类
    SocketClientHandler监听类
    