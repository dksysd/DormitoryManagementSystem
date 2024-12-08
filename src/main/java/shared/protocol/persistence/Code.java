package shared.protocol.persistence;

public interface Code extends HeaderElement {
    enum RequestCode implements Code {
        LOGIN, LOGOUT, REFRESH_SESSION
    }

    enum ResponseCode implements Code {
        OK,
    }

    enum ErrorCode implements Code {

    }

    enum ValueCode implements Code {
        ID, PASSWORD
    }
}
