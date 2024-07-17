package codesquad.application.session;

import codesquad.application.domain.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    // ToDo: Session 클래스 생성
    private Map<String, User> session;
    private static SessionManager instance;
    private SessionManager(){
        session = new ConcurrentHashMap<>();
    }

    // ToDo: multi thread
    public static SessionManager getInstance(){
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    public String addUser(User user){
        String key = generateKey();
        session.put(key, user);
        return key;
    }

    public void deleteUser(String key){
        session.remove(key);
    }

    public boolean hasUser(String key){
        return session.containsKey(key);
    }

    public User getUser(String key){
        return session.get(key);
    }
}
