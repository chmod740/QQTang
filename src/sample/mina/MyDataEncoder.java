package sample.mina;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MyDataEncoder implements ProtocolEncoder {

    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out) throws Exception {

        if (message != null){

            MyData myData = (MyData)message;
            IoBuffer ioBuffer;
            ioBuffer = IoBuffer.allocate(1024).setAutoExpand(true);
            ioBuffer.setAutoShrink(true);
            ioBuffer.setAutoExpand(true);
            ioBuffer.putInt(myData.mode);
            ioBuffer.putInt(myData.x);
            ioBuffer.putInt(myData.y);

            ioBuffer.flip();
            out.write(ioBuffer);
        }
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {
    }
}
