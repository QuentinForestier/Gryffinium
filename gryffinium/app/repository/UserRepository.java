package repository;

import controllers.UserController;
import io.ebean.DB;
import models.User;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class UserRepository {
    private final DatabaseExecutionContext executionContext;

    @Inject
    public UserRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public User findByEmail(String email) {
        return supplyAsync(() -> DB.find(User.class)
                .where()
                .eq("email", email)
                .findOne(), executionContext).join();
    }

    /**
     * Save a user.
     * @param user
     * @return
     */
    public CompletionStage<User> create(User user) {
        return supplyAsync(() -> {
            user.save();
            return user;
        }, executionContext);
    }

    public CompletionStage<Map<String,String>> options(){
        return supplyAsync(() -> DB.find(User.class).orderBy("name").findList(), executionContext)
                .thenApply(users -> {
                    HashMap<String, String> options = new HashMap<>();
                    for(User u : users){
                        options.put(u.getId().toString(), u.getName());
                    }
                    return options;
                });
    }
}
