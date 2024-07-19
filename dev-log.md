
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
    - Content-Length
        - 필요성
```
Content-Length: body길이\r\n
\r\n
```

- body

## multi thread/process 가 아닌 Server에 동시 접속이 발생하면
- Caused by: java.net.ConnectException: Connection refused

# ServerSocket 
## new

```java
public ServerSocket(int port) throws IOException {
    this(port, 50, null);
}

public ServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
    setImpl();
    if (port < 0 || port > 0xFFFF)
        throw new IllegalArgumentException(
                "Port value out of range: " + port);
    if (backlog < 1)
        backlog = 50;
    try {
        bind(new InetSocketAddress(bindAddr, port), backlog);
    } catch(SecurityException e) {
        close();
        throw e;
    } catch(IOException e) {
        close();
        throw e;
    }
}
```

## bind & listen
- 주소, 포트 할당
```java
public void bind(SocketAddress endpoint, int backlog) throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (isBound())
            throw new SocketException("Already bound");
        if (endpoint == null)
            endpoint = new InetSocketAddress(0);
        if (!(endpoint instanceof InetSocketAddress epoint))
            throw new IllegalArgumentException("Unsupported address type");
        if (epoint.isUnresolved())
            throw new SocketException("Unresolved address");
        if (backlog < 1)
          backlog = 50;
        try {
            @SuppressWarnings("removal")
            SecurityManager security = System.getSecurityManager();
            if (security != null)
                security.checkListen(epoint.getPort());
            getImpl().bind(epoint.getAddress(), epoint.getPort());
            getImpl().listen(backlog);
            bound = true;
        } catch(SecurityException e) {
            bound = false;
            throw e;
        } catch(IOException e) {
            bound = false;
            throw e;
        }
    }

```

## accept

```java
    public Socket accept() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!isBound())
            throw new SocketException("Socket is not bound yet");
        Socket s = new Socket((SocketImpl) null);
        implAccept(s);
        return s;
    }
```


# Jar 파일
- File 로드 오류
  - 

# multipart form data

```
POST /upload HTTP/1.1
Host: example.com
Content-Type: multipart/form-data; boundary=---123456789012345
Content-Length: [전체 데이터의 길이]

-----123456789012345
Content-Disposition: form-data; name="username"

JohnDoe
-----123456789012345
Content-Disposition: form-data; name="file"; filename="image.jpg"
Content-Type: image/jpeg

[이미지 파일의 바이너리 데이터]
-----123456789012345--
```


# 정규 표현식
```
. : 줄바꿈을 제외한 모든 문자
+ : 앞 문자가 하나 이상 반복
* : 앞 문자가 0 이상 반복
(): 캡쳐 그룹, 매칭 시 group(index) 로 찾을 수 있다
[] : 문자 집합, 해당 위치에 올 수 있는 문자들을 정의
^ : 뒤의 문자를 제외한 문자들

\\s : 공백
\\w : 문자(알파벳, 숫자, 밑줄, 등)
\\( : 열린 괄호
\\) : 닫힌 괄호
\\n : 줄바꿈
```

# 패턴 컴파일
- Pattern.CASE_INSENSITIVE
  - 대소문자 구분 없이 동일 취급
- Pattern.DOTALL
  - 정규표현식 . 이 줄바꿈 까지 포함 시킨다