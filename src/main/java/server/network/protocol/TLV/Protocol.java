package server.network.protocol.TLV;

public record Protocol(byte type, byte code, int dataLength, byte[] data) {

    /*
    type : 1 byte
    code : 1 byte
    dataLength : 4 byte

    data : depends on dataLength

    <Client>
    type : Request
    code : Login
    dataLength : ?

    data :
        type: Value
        code : Id
        dataLength: ?
        data : '123'

        type Value
        code : Pw
        dataLength:?
        data : '123'

     ---
     <Server>
     type : Error
     code : Invalid_value
     dataLength : 0
     data :

     1. Req 프로토콜 받음
     2. 파싱
     3. Map<Value, Object>
     4. 내부적으로 로직 처리
     5. Map<Value, Object>
     6. Res 프로토콜 변환
     7. 보냄

     String id = req.getValue(ID)
     String pw = req.getValue(PASSWORD)
    => LoginDto('123', '123')

    Object getValue(RequestValue);
     */

}
