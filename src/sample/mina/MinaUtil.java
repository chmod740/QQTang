package sample.mina;


import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class MinaUtil {
    /**
     * 客户端用：
     * 服务器IP地址
     * */
    private String serverAddr = null;

    /**
     * 标记
     * */
    private Boolean isServer = null;


    /**
     * 一个server实例
     * */
    private static MinaUtil minaUtilServer = null;

    /**
     * 一个client实例
     * */
    private static MinaUtil minaUtilClient = null;

    /**
     * 客户端用：
     * IoSession实例
     * */
    private IoSession session = null;

    /**
     * 一个简单的监听器，用以实现回调
     * */
    private SimpleMinaListener simpleListener = null;




    /**
     * 服务器用：
     * NioSocketAcceptor
     * */
    private static NioSocketAcceptor acceptor = null;

    /**
     * 会话列表
     * */
    private List<IoSession>sessions = new ArrayList<>();

    class MessageAndIosession{
        public IoSession ioSession;
        public Object message;
    }

    public static MinaUtil getInstance(SimpleMinaListener simpleListener, Boolean isServer, String serverAddr){
        if (isServer){
            if (minaUtilServer == null){
                minaUtilServer = new MinaUtil(simpleListener,isServer,null);
            }
            return minaUtilServer;
        }else {
            if (minaUtilClient == null){
                minaUtilClient = new MinaUtil(simpleListener,isServer,serverAddr);
            }
            return minaUtilClient;
        }


    }



    /**
     * 实现单例模式，私有构造函数
     * */
    private MinaUtil(SimpleMinaListener simpleListener, Boolean isServer, String serverAddr){
        this.isServer = isServer;
        this.simpleListener = simpleListener;
        this.serverAddr = serverAddr;
        if (!isServer) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            NioSocketConnector connector = new NioSocketConnector();
                            connector.setHandler(new MyClientHandler());
                            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyDataFactory()));
                            ConnectFuture future;
                            if (serverAddr != null){
                                future = connector.connect(new InetSocketAddress(serverAddr, 9191));
                            }else {
                                future = connector.connect(new InetSocketAddress("192.168.43.1", 9191));
                            }

                            future.awaitUninterruptibly();
                            MinaUtil.this.session = future.getSession();
                            break;

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();

        }else {
            if (acceptor == null){
                acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
            }
            acceptor.setReuseAddress(true);
            acceptor.getSessionConfig().setReadBufferSize(8192);

            acceptor.setHandler(new MyServerHandler());
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyDataFactory()));



            //设置客户机超时提醒
//            acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE, 5);
            try {
                acceptor.bind(new InetSocketAddress(9191));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    /**
     * 发送消息
     * */
    public Boolean send(Object obj){
        if (isServer){
            if (sessions.size() == 0){
                return false;
            }
            Boolean flag = true;
            for(IoSession session: sessions){
                try {
                    session.write(obj);
                }catch (Exception e){
                    e.printStackTrace();
                    flag = false;
                }
            }
            return flag;
        }else {
            try {
                if (session == null){

                }else{
                    session.write(obj);
                }

            }catch (Exception e){
                return false;
            }
            return true;
        }
    }

    /**
     * 发送消息
     * */
    public Boolean send(Object obj,IoSession ioSession){
        try {
            ioSession.write(obj);
        }catch (Exception e){
            return false;
        }
        return true;
    }



    /**
     * handler 用以实现各种客户端消息的回调
     * */
    class MyClientHandler extends IoHandlerAdapter {

        public void exceptionCaught(IoSession session, Throwable cause)
                throws Exception {
            super.exceptionCaught(session, cause);
            System.out.println(session.getId());
            System.out.println("messageCaught");
            System.out.println(cause.getMessage());
        }

        public void messageReceived(IoSession session, Object message)
                throws Exception {
            simpleListener.onReceive(message,session);
        }

        public void messageSent(IoSession session, Object message) throws Exception {
        }

        public void sessionClosed(IoSession session) throws Exception {
            simpleListener.offLine();
            System.out.println(session.getId());
            System.out.println("sessionClosed");
            MinaUtil.this.session = null;
            //实现断线重连
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                            NioSocketConnector connector = new NioSocketConnector();
                            connector.setHandler(new MyClientHandler());
                            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MyDataFactory()));
                            ConnectFuture future;
                            if (serverAddr != null) {
                                future = connector.connect(new InetSocketAddress(serverAddr, 9191));
                            } else {
                                future = connector.connect(new InetSocketAddress("192.168.43.1", 9191));
                            }
                            future.awaitUninterruptibly();
                            MinaUtil.this.session = future.getSession();
                            System.out.println("连接");
                            break;
                        } catch (Exception ex) {
                            System.out.println("连接:" + ex.getMessage());
                        }
                    }
                }
            }).start();
        }

        public void sessionCreated(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionCreated");
        }

        public void sessionIdle(IoSession session, IdleStatus status)
                throws Exception {
            //客户端空闲的时候进行回调
            System.out.println(session.getId());
            System.out.println("sessionIdle");
        }

        public void sessionOpened(IoSession session) throws Exception {
            simpleListener.onLine("对方玩家("+ session.getRemoteAddress().toString().replaceAll("/","") +")已上线");
            System.out.println(session.getId());
            System.out.println("sessionOpened");
        }

    }


    /**
     * handler 用以实现各种服务器消息的回调
     * */
    class MyServerHandler extends IoHandlerAdapter {

        public void exceptionCaught(IoSession session, Throwable cause)
                throws Exception {
            super.exceptionCaught(session, cause);
            System.out.println(session.getId());
            System.out.println("messageCaught");
            System.out.println(cause.getMessage());
        }

        public void messageReceived(IoSession session, Object message)
                throws Exception {
            simpleListener.onReceive(message,session);
        }

        public void messageSent(IoSession session, Object message) throws Exception {
        }

        public void sessionClosed(IoSession session) throws Exception {
            simpleListener.offLine();
            System.out.println(session.getId());
            System.out.println("sessionClosed");
            sessions.remove(session);

        }

        public void sessionCreated(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionCreated");
            sessions.add(session);
        }

        public void sessionIdle(IoSession session, IdleStatus status)
                throws Exception {
            //客户端空闲的时候进行回调
            System.out.println(session.getId());
            System.out.println("sessionIdle");
        }

        public void sessionOpened(IoSession session) throws Exception {
            System.out.println(session.getId());
            System.out.println("sessionOpened");
            simpleListener.onLine("对方玩家("+ session.getRemoteAddress().toString().replaceAll("/","") +")已上线");
        }
    }
}
