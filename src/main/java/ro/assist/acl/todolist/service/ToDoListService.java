package ro.assist.acl.todolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import ro.assist.acl.todolist.Dto.UserTodoListDto;
import ro.assist.acl.todolist.model.TodoList;

import ro.assist.acl.todolist.repository.TodoItemsRepository;
import ro.assist.acl.todolist.repository.TodoListRepository;

import java.util.Collections;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class ToDoListService {
    private final TodoListRepository repository;
    private final TodoItemsRepository itemsRepository;

    public Slice<UserTodoListDto> getAllTodoLists(String orgId, String userId, Pageable pageable) {
        try {
            return repository.findByOrganizationIdAndUserId(orgId, userId, pageable)
                    .map(todoList -> new UserTodoListDto(todoList.getId(), todoList.getTitle()));
        } catch (DataAccessException e) {
            throw new DataAccessException("Error while fetching todo lists.") {
                @Override
                public synchronized Throwable getCause() {
                    return super.getCause();
                }
            };
        }
    }

    public TodoList addTodoList(String organizationId, String userId, UserTodoListDto dto) {
        try {
            if (dto == null) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"The entity provided is null.");
            }
            if (dto.getTitle().isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"The content of the entity-{title} provided is null.");
            }
            var newTodoList = new TodoList(UUID.randomUUID().toString(), userId, organizationId, dto.getTitle());
            repository.save(newTodoList);
            return newTodoList;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.toString(),e);
        }
    }

    public TodoList updateTodoList(String orgId,String userId,String listId, UserTodoListDto todoListDto)
    {
        try {
            TodoList todoList = repository.findById(listId).orElse(null);
            if (todoList == null) {
                throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
            }
            if(!orgId.equals(todoList.getOrganizationId())||!userId.equals(todoList.getUserId()))
            {
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
            }
            todoList.setTitle(todoListDto.getTitle());
            repository.save(todoList);
            return todoList;
        } catch (DataAccessException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,e.toString());
        }
    }

    public boolean deleteTodoList(String orgId,String userId,String listId) {
        try {
            TodoList todoList = repository.findById(listId).orElse(null);
            if (todoList == null) {
                throw new HttpServerErrorException(HttpStatus.NOT_FOUND);
            }
            if(!orgId.equals(todoList.getOrganizationId())||!userId.equals(todoList.getUserId()))
            {
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
            }
            itemsRepository.deleteByListId(listId);
            repository.delete(todoList);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new HttpServerErrorException(HttpStatus.CONFLICT,e.toString());
        } catch (DataAccessException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,e.toString());
        }
    }

}