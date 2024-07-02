# 1단계 - index.html 응답

## 기능요구사항
### 정적인 html 파일 응답
- [ ] http://localhost:8080/index.html 로 접속했을 때 src/main/resources/static 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

### HTTP Request 내용 출력
- [ ] 서버로 들어오는 HTTP Request의 내용을 읽고 **적절하게 파싱해서** 로거(log.debug)를 이용해 출력한다.

### 제한 사항
- [ ] Lombok은 사용하지 않는다.
- [ ] 로거, Junit5, AssertJ 외 불필요한 외부 라이브러리는 사용을 지양한다.
- [ ] JDK의 nio는 사용하지 않는다.
- [ ] Controller, Service, DTO, Repository 등 MVC와 관련된 Naming은 사용하지 않는다.