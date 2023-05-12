package ro.assist.acl.todolist.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ro.assist.acl.todolist.model.TodoItem;

import java.util.Optional;

@Repository
public interface TodoItemsRepository extends MongoRepository<TodoItem, String> {

    Slice<TodoItem> findByListId(String listId,Pageable pageable);
    Optional<TodoItem> findByIdAndListId(String itemId,String listId);
    void deleteByListId(String listId);
}