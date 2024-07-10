package codesquad.application.session;

import codesquad.application.domain.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private Map<String, User> session;
    private static Session instance;
    private Session(){
        session = new ConcurrentHashMap<>();
    }

    public static Session getInstance(){
        if(instance == null){
            instance = new Session();
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
