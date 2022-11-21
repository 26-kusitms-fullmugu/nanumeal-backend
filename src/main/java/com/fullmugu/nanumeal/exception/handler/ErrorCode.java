package com.fullmugu.nanumeal.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    NOT_FOUND(404, "COMMON_ERR-404", "PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500, "COMMON-ERR-500", "INTER SERVER ERROR"),
    EMAIL_DUPLICATION(400, "MEMBER-ERR-400", "EMAIL DUPLICATED"),
    UNAUTHORIZED(401, "AUTHORIZATION-ERR-401", "NO AUTHORIZATION"),
    FORBIDDEN(403, "AUTHORIZATION-ERR-403", "NO AUTHORIZATION"),
    SERVICE_UNAVAILABLE(503, "SERVICE-ERR-503", "THIS SERVICE ENDED"),
    DB_ERROR(600, "DATA-ERR-600", "NO DATA"),
    BAD_REQUEST(400, "AUTHORIZATION-ERR-400", "BAD REQUEST");
    //상태코드를 담을 클래스
    //200번대는 성공을 의미
//    public static final int OK = 200;
//    public static final int CREATED = 201;
//    public static final int NO_CONTENT = 204; //수정, 삭제 시 데이터 반환이 필요 없을 때, content(data)가 없으므로
//    public static final int BAD_REQUEST = 400;
//    public static final int UNAUTHORIZED = 401;
//    public static final int FORBIDDEN = 403;
//    public static final int NOT_FOUND = 404;
//    public static final int INTERNAL_SERVER_ERROR = 500;
//    public static final int SERVICE_UNAVAILABLE = 503;
//    public static final int DB_ERROR = 600;

    private int status;
    private String errorCode;
    private String message;

    //    api 요청이 성공적으로 완료된 경우
//
//2로 시작되는 상태 코드
//200(OK) : 요청 성공의 기본 상태 코드
//201(No Content) : body에 응답 내용이 없을 경우 이용
//
//
//    클라이언트 측 에러 응답
//
//4로 시작되는 상태 코드
//400(Bad Request) : request 형식 틀렸을 경우
//401(Unathorized) : 리소스 접근 권한 없는 경우
//403(Forbidden) : 해당 리소스에 접근하는 것이 허락되지 않을 경우
//404(Not Found) : 존재하지 않는 URI
//405(Method Not Allowed) : 존재하지 않는 request method
//406(Not Acceptable) : request의 Accept header에 설정한 MIME 타입이 지원 불가능한 경우
//414(URI Too Long) : 요청한 URI가 너무 김
//
//
//    서버 측 에러 응답
//
//5로 시작되는 상태 코드
//500(Internal Server Error) : 서버에서 에러가 발생한 경우에 설정되는 기본 상태 코드
//503(Service Unavailable) : 외부 서비스가 현재 멈춘 상태 이거나 이용할 수 없는 서비스


}
