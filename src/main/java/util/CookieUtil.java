package util;

public class CookieUtil {
    public static String sessionId = "sid";
    // 테스트 함수 만들기
    public static String getCookieValue(String cookie, String key) {
        String[] cookies = cookie.split("; ");
        for (String c : cookies) {
            String[] keyValue = c.split("=");
            if (keyValue[0].equals(key)) {
                return keyValue[1];
            }
        }
        return null;
    }
}
