package server.controller;


import shared.protocol.persistence.*;

import static server.util.ProtocolValidator.getIdByIdBySessionId;


public class UserController {
    /**
     * @param protocol header(type:request, dataType: TLV, code: GET_USER_INFO, dataLength:)
     *                 data:
     *                 children<
     *                 header(type: value, dataType: string, code: SESSION_ID, dataLength:)
     *                 data: sessionId
     *                 >
     * @return protocol header(type:response, dataType: TLV, code: OK, dataLength:)
     * data:
     * children<
     * 1 header(type: value, dataType: string, code: ID, dataLength:)
     * data: 아이디
     * 2 header(type: value, dataType: string, code: USER_NAME, dataLength:)
     * data: 이름
     * 3 header(type: value, dataType: string, code: PHONE_NUMBER, dataLength:)
     * data: 전화번호
     * 4 header(type: value, dataType: string, code: GENDER_NAME, dataLength:)
     * data: 성별
     * 5 header(type: value, dataType: string, code: DO, dataLength:)
     * data: 도(주소)
     * 6 header(type: value, dataType: string, code: SI, dataLength:)
     * data: 시(주소)
     * 7 header(type: value, dataType: string, code: DETAIL_ADDRESS, dataLength:)
     * data: 상세주소
     * >
     */
    public static Protocol<?> getUserInfo(Protocol<?> protocol) {
        Header header = new Header();
        Header childHeader = new Header(Type.VALUE,DataType.STRING,Code.ValueCode.ID,0);
        Protocol<?> resProtocol = new Protocol<>();
        Protocol<?> c1Protocol = new Protocol<>(childHeader,"");
        Protocol<?> c2Protocol = new Protocol<>(childHeader,"");
        Protocol<?> c3Protocol = new Protocol<>(childHeader,"");
        Protocol<?> c4Protocol = new Protocol<>(childHeader,"");
        Protocol<?> c5Protocol = new Protocol<>(childHeader,"");
        Protocol<?> c6Protocol = new Protocol<>(childHeader,"");
        Protocol<?> c7Protocol = new Protocol<>(childHeader,"");
        String id;
        String name;
        String phone;
        String gender;
        String doo;
        String si;
        String detailAddress;
        //todo List<String>(name:phone:gender:do:si:detailAdress)getUserInfo(String Uid)
        id = getIdByIdBySessionId((String) protocol.getChildren().getFirst().getData());
        header.setType(Type.RESPONSE);
        header.setDataType(DataType.TLV);
        if(id!=null){
            header.setCode(Code.ResponseCode.OK);

        }


    }


}