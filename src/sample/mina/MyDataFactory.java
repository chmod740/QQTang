package sample.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyDataFactory implements ProtocolCodecFactory {
    private MyDataEncoder myDataEncoder;
    private MySimpleDataDecoder mySimpleDataDecoder;

    public MyDataFactory(){
        myDataEncoder = new MyDataEncoder();
        mySimpleDataDecoder = new MySimpleDataDecoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return myDataEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return mySimpleDataDecoder;
    }
}
