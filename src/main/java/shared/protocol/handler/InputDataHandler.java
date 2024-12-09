package shared.protocol.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.exception.IllegalBufferSizeException;
import server.util.RemoteAddressResolver;
import shared.protocol.persistence.*;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

@AllArgsConstructor
@Getter
public class InputDataHandler implements CompletionHandler<Integer, ByteBuffer> {
    protected final Header header;
    protected final ByteBuffer headerBuffer;
    protected final InputHeaderHandler inputHeaderHandler;

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (result == -1) {
            inputHeaderHandler.getCloseClientConsumer().accept(inputHeaderHandler.getClient());
            return;
        }

        if (buffer.position() != header.getDataLength()) {
            throw new IllegalBufferSizeException("Invalid data size (expected : " + header.getDataLength() + ", value : " + result + ")");
        }

        System.out.println("Read data from " + RemoteAddressResolver.getRemoteAddress(inputHeaderHandler.getClient()));

//        buffer.flip();
//        Protocol<?> protocol = ProtocolParser.parseProtocol(header, buffer);
//        inputHeaderHandler.getCompleteConsumer().accept(inputHeaderHandler.getClient(), protocol);

        headerBuffer.clear();
        inputHeaderHandler.getClient().read(headerBuffer, headerBuffer, inputHeaderHandler);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        exc.printStackTrace();
    }
}
