//package ro.assist.acl.todolist.service;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.SliceImpl;
//import ro.assist.acl.todolist.Dto.UserTodoListDto;
//import ro.assist.acl.todolist.model.TodoList;
//import ro.assist.acl.todolist.repository.TodoListRepository;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ToDoListServiceTest {
//
//    @Mock
//    private TodoListRepository repository;
//
//    @InjectMocks
//    private ToDoListService service;
//
//    @Test
//    void todoLists() {
//        doReturn(new SliceImpl<>(List.of(new TodoList("testId", "testUserId", "testOrgId", "testTitle"))))
//                .when(repository).findByOrganizationIdAndUserId(any(), any(), any());
//        var result = service.todoLists("", "", Pageable.unpaged());
//        assertEquals(1, result.getContent().size());
//        var todoListDto = result.getContent().get(0);
//        assertEquals("testTitle", todoListDto.getTitle());
//    }
//
//    @Test
//    void addTodoList() {
//        doReturn(mock(TodoList.class)).when(repository).save(any());
//        service.addTodoList("testOrgId", "testUserId", new UserTodoListDto(null, "testTitle"));
//        var todoListArgumentCaptor = ArgumentCaptor.forClass(TodoList.class);
//
//        verify(repository, times(1)).save(todoListArgumentCaptor.capture());
//        var todoList = todoListArgumentCaptor.getValue();
//        assertEquals("testTitle", todoList.getTitle());
//        assertEquals("testOrgId", todoList.getOrganizationId());
//    }
//}