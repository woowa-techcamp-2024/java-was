package codesquad.application.datahandler;

import codesquad.application.domain.User;
import codesquad.webserver.annotation.DataHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@DataHandler("UserDataHandlerInMemory")
public class UserDataHandlerInMemory implements UserDataHandler{
    private Map<String, User> store = new ConcurrentHashMap<>();

    @Override
    public void insert(User user) {
        String key = UUID.randomUUID().toString();
        User insertUser = new User(key, user.getUsername(), user.getPassword(), user.getNickname());
        store.put(key, insertUser);
    }

    @Override
    public List<User> getAll() {
        return store.values().stream().toList();
    }


    @Override
    public Optional<User> getByUsername(String username) {
        return store.values().stream().filter(e -> e.getUsername().equals(username)).findFirst();
    }
}
