# Java WAS

## 2024 우아한 테크캠프 프로젝트 WAS

### 구현 목록

#### 1단계

- [x] http://localhost:8080/index.html로 요청이 오면 `resources/static/index.html` 파일을 읽어 응답
    - [x] 만약 존재하지 않은 파일을 요청하면 `404 Not Found`로 응답
- [x] HTTP request를 적절히 파싱해 `HttpRequest` 객체와 `HttpHeaders` 객체 생성
    - [x] HTTP request를 logger를 사용해 debug 레벨로 출력
- [x] `ExecutorService`(`ThreadPoolExecutor`)를 사용해 10개의 스레드로 사용자 요청 멀티 스레드 처리
