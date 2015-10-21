
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.logging.Logger;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

public class MyMessageInbound extends MessageInbound {
    private int userIdName = 0;
    private Logger logger = Logger.getLogger(MyMessageInbound.class.getName());

    public int getUserIdName() {
        return userIdName;
    }

    protected void onOpen(WsOutbound outbound) {
        super.onOpen(outbound);
        userIdName = outbound.hashCode();
        EchoServlet.getSocketList().add(this);
        logger.info("Server onOpen");
    }

    protected void onClose(int status) {
        EchoServlet.getSocketList().remove(this);
        super.onClose(status);
        logger.info("Server onClose");
    }

    // 有二进制消息数据到达，比如音频等文件传输
    @Override
    protected void onBinaryMessage(ByteBuffer buffer) throws IOException {
        logger.info("Binary Message Receive: " + buffer.remaining());
    }

    @Override
    protected void onTextMessage(CharBuffer buffer) throws IOException {
        String msgOriginal = buffer.toString();

        int startIndex = msgOriginal.indexOf("!@#$%");
        String nikeName = msgOriginal.substring(0, startIndex);
        String textMsg = msgOriginal.substring(startIndex + 5);
        // 将字符数组包装到缓冲区中
        // 给定的字符数组将支持新缓冲区；即缓冲区修改将导致数组修改，反之亦然

        String countMsg = EchoServlet.getSocketList().size() + "人同时在线";
        logger.info("Server onTextMessage: " + countMsg + nikeName + ":" + textMsg);

        String msg1 = nikeName + ": " + textMsg;
        String msg2 = "我: " + textMsg;

        for (MyMessageInbound messageInbound : EchoServlet.getSocketList()) {
            CharBuffer msgBuffer1 = CharBuffer.wrap(msg1);
            CharBuffer msgBuffer2 = CharBuffer.wrap(msg2);

            WsOutbound outbound = messageInbound.getWsOutbound();
            if (messageInbound.getUserIdName() != this.getUserIdName()) {
                outbound.writeTextMessage(msgBuffer1);
                outbound.flush();
            } else {
                outbound.writeTextMessage(msgBuffer2);
            }
        }
    }
}