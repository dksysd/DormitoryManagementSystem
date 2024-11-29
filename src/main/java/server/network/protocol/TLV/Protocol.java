package server.network.protocol.TLV;

public record Protocol(byte type, byte code, int dataLength, byte[] data) {
    /*
    type : 1 byte
    code : 1 byte
    dataLength : 4 byte

    data : depends on dataLength
     */
}
