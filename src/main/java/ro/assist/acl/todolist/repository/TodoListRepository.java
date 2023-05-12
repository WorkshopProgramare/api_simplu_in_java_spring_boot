package ro.assist.acl.todolist.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ro.assist.acl.todolist.model.TodoList;



@Repository
public interface TodoListRepository extends MongoRepository<TodoList, String> {
    Slice<TodoList> findByOrganizationIdAndUserId(String organizationId, String userId, Pageable pageable);


}