package birthday.repository;

import birthday.model.Friend;

import java.util.List;

public interface FriendRepository {
    List<Friend> findAll();
}
