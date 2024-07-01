# Java WAS

2024 우아한 테크캠프 프로젝트 WAS

# 제약사항
- 로깅, assertj 외 라이브러리 금지
- java 17
- mvc naming 금지
- nio 금지
- System.out.println 금지

# 요구 사항
## 1-1
- http://localhost:8080/index.html 접속시 src/main/resources/static 디렉토리의 index.html 반환
- http request 내용 로깅

## 1-2
- src/main/resources/static 폴더의 html, css, js, ico, png, jpg 응답 지원

## 1-3
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/register.html 로 이동,
- 회원가입 폼에서 가입 버튼 클릭시 다음 형태를 서버로 전달
  - /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
- url 파싱해 User 클래스 저장
- 

# 지식
- \r\n 이 http 개행 표준

## http request 구조
- start line

```
GET /index HTTP/1.1
\r\n
```

- Headers

```
HOST: localhost
User-Agent: chrome
Accept: 응답타입
Connection: 응답 후 연결 유지에 관한 지시
Content-Type: application/json
Content-Length: body길이
\r\n
```

- Body

```
```

## http response 구조
- status line
```
HTTP/1.1 404 Not Found\r\n
```

- Header
  - 마지막 개행 2줄
```
Content-Length: body길이\r\n
\r\n
```

- body
