package codesquad.application.database;

import codesquad.application.domain.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDatabase {
    private static UserDatabase instance;
    private Map<String, User> db;

    private UserDatabase(){
        db = new ConcurrentHashMap<>();
    }

    public static UserDatabase getInstance(){
        if(instance == null){
            instance = new UserDatabase();
        }
        return instance;
    }


    public void add(String key, User user){
        db.put(key, user);
    }

    public User get(String key){
        return db.get(key);
    }
}
