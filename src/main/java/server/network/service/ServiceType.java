package server.network.service;

public enum ServiceType {

    ADMISSION_SERVICE(1, "ADMISSION SERVICE"),
    //입사 신청 서비스
    CHECKOUT_SERVICE(2, "CHECKOUT SERVICE"),
    //퇴사 신청 서비스
    SCHEDULE_MANAGEMENT_SERVICE(3, "SECHEDULE MANAGEMENT SERVICE"),
    //일정 관리 서비스
    ROOM_REGISTRATION_SERVICE(4, "ROOM REGISTRATION SERVICE"),
    //방 등록 서비스
    MEAL_REGISTRATION_SERVICE(5, "MEAL REGISTRATION SERVICE"),
    //식사 등록 서비스
    SELECTION_SERVICE(6, "SELECTION SERVICE"),
    //입사자 선발 서비스
    CHECKOUT_MANAGEMENT_SERVICE(7, "CHECKOUT MANAGEMENT SERVICE");
    //퇴사자 처리 서비스
    private static final ServiceType[] VALUES = values();


    private final int value;
    private final String reasonPhrase;

    private ServiceType(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}








