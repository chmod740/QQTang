package sample.mina;

import org.apache.mina.core.session.IoSession;

public interface SimpleMinaListener {
    /**
     * 收到消息
     * */
    public void onReceive(Object obj, IoSession ioSession);

    /**
     * 对方上线
     * */
    public void onLine(String msg);


    /**
     * 对方下线
     * */
    public void offLine();
}
