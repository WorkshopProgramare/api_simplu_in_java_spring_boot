package ro.assist.acl.todolist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;
import ro.assist.acl.todolist.Dto.TodoItemsDto;
import ro.assist.acl.todolist.model.TodoItem;
import ro.assist.acl.todolist.model.TodoList;
import ro.assist.acl.todolist.repository.TodoItemsRepository;
import ro.assist.acl.todolist.repository.TodoListRepository;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class TodoItemsService {

    private final TodoItemsRepository repository;
    private final TodoListRepository listRepository;

    //RETURN ALL ITEMS OF A TODO LIST
    public Slice<TodoItemsDto> findAllByListId(String orgId,String userId,String listId, Pageable pageable) {
        try {
            if (pageable==null) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
            TodoList todoList = listRepository.findById(listId).orElse(null);
            if (todoList == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            if(!orgId.equals(todoList.getOrganizationId())||!userId.equals(todoList.getUserId()))
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            return repository.findByListId(listId, pageable)
                    .map(todoItem -> new TodoItemsDto(
                            todoItem.getId(),
                            todoItem.getListId(),
                            todoItem.getPlaceInList(),
                            todoItem.isCompleted(),
                            todoItem.getContent()));
        } catch (DataAccessException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,e.toString());
        }
    }

    //CREATE ITEM
    public TodoItem create(String orgId, String userId,String listId, int place, boolean completed, String content) {
        try {
            if (content.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, content);
            }
            TodoList todoList = listRepository.findById(listId).orElse(null);
            if (todoList == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            if (!orgId.equals(todoList.getOrganizationId()) || !userId.equals(todoList.getUserId())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            var newTodoItem = new TodoItem(UUID.randomUUID().toString(), listId, place, completed, content);
            repository.save(newTodoItem);
            return newTodoItem;
        } catch (DataIntegrityViolationException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    //UPDATE ITEM
    public TodoItem update(String orgId, String userId, String listId, String itemId, TodoItemsDto todoItemDto) {
        try {
            if (todoItemDto == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            TodoList todoList = listRepository.findById(listId).orElse(null);
            if (todoList == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            if(!orgId.equals(todoList.getOrganizationId())||!userId.equals(todoList.getUserId()))
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            TodoItem updatedTodoItem = repository.findByIdAndListId(itemId, listId).orElse(null);
            if (updatedTodoItem == null) {
                return create(orgId,userId,listId,todoItemDto.getListPlace(),todoItemDto.isCompleted(),todoItemDto.getContent());
            } else {
                updatedTodoItem.setCompleted(todoItemDto.isCompleted());
                updatedTodoItem.setContent(todoItemDto.getContent());
                updatedTodoItem.setPlaceInList(todoItemDto.getListPlace());
                repository.save(updatedTodoItem);
                return updatedTodoItem;
            }
        } catch (DataAccessException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    //DELETE ITEM
    public boolean delete(String orgId, String userId, String listId, String itemId) {
        try {
            TodoList todoList = listRepository.findById(listId).orElse(null);
            if (todoList == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            //we can call the organizationAPI and usersAPI to
            //verify if the user is authenticated
            if(!orgId.equals(todoList.getOrganizationId())||!userId.equals(todoList.getUserId()))
            {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            TodoItem todoItem = repository.findByIdAndListId(itemId, listId).orElse(null);
            if (todoItem == null) {
                return true;
            }
            repository.delete(todoItem);
            return true;
        } catch (DataAccessException e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
}

