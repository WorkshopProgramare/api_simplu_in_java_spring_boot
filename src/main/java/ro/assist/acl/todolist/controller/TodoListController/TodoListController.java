package ro.assist.acl.todolist.controller.TodoListController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.assist.acl.todolist.common.CustomPage;
import ro.assist.acl.todolist.Dto.UserTodoListDto;
import ro.assist.acl.todolist.model.TodoList;
import ro.assist.acl.todolist.service.ToDoListService;

import javax.validation.Valid;

@CrossOrigin(origins = "*")

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizations/{organizationId}")
@Tag(name = "ToDoLists", description = "ToDoLists API")
@Validated
public class TodoListController {
    private final ToDoListService service;

    @PostMapping("/users/{userId}/todoLists/")
    @Operation(
            summary = "Add a new todo list",
            description = "This endpoint allows the user to add a new todo list ",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created new todo list",content = @Content(schema = @Schema(implementation = TodoList.class)) ),
                    @ApiResponse(responseCode = "400", description = "Missing required fields in request"),
                    @ApiResponse(responseCode = "500", description = "Error while saving todo list")
            })
    @ApiResponse(content = @Content(schema = @Schema(implementation = ResponseEntity.class)))
    public ResponseEntity<TodoList> addUserTodoList(@PathVariable String organizationId,
                                                    @PathVariable String userId,
                                                    @Valid @RequestBody UserTodoListDto dto) {

        return new ResponseEntity<>(service.addTodoList(organizationId, userId, dto), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/todoLists")
    @Operation(
            summary = "Get all todo lists for a user in an organization",
            description = "Retrieves a paginated list in a custom page format of todo lists for a user " +
                    "in a specific organization. Returns a slice of UserTodoListDto " +
                    "objects. If it is empty then no todo lists were found for the user in " +
                    "the specified organization or an internal server error has occurred " +
                    "or the required variables {organizationId,userId} where empty. " +
                    "Check the log for more info."
    )
    @ApiResponse(content = @Content(schema = @Schema(implementation = CustomPage.class)))
    public Slice<UserTodoListDto> getUserTodoLists(@PathVariable String organizationId,
                                                   @PathVariable String userId,
                                                   @ParameterObject @PageableDefault(size = 5, sort = "id") Pageable pageable)
    {
        return new CustomPage<>(service.getAllTodoLists(organizationId, userId, pageable));
    }

    @PutMapping("/users/{userId}/todoLists/{todoListId}")
    @Operation(
            summary = "Update a user Todo List",
            description = "Updates a Todo List by its Id and returns the updated Todo List",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todo List updated successfully", content = @Content(schema = @Schema(implementation = TodoList.class))),
                    @ApiResponse(responseCode = "400", description = "Missing required fields in request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, {organizationId,userId} variables don't " +
                            "match the ones from the specified todo list."),
                    @ApiResponse(responseCode = "404", description = "Todo List not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<TodoList> updateUserTodoList(@PathVariable String organizationId,
                                                       @PathVariable String userId,
                                                       @PathVariable String todoListId,
                                                       @RequestBody UserTodoListDto userTodoListDto)
    {
        return new ResponseEntity<>(service.updateTodoList(organizationId,userId,todoListId,userTodoListDto),HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/todoLists/{todoListId}")
    @Operation(
            summary = "Delete Todo List",
            description = "Deletes a Todo List by its Id and returns the deleted Todo List",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Todo List deleted successfully"),
                    @ApiResponse(responseCode = "400", description = "Missing required fields in request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, {organizationId,userId} variables don't " +
                            "match the ones from the specified todo list."),
                    @ApiResponse(responseCode = "404", description = "Todo List not found"),
                    @ApiResponse(responseCode = "409", description = "DataIntegrityViolationException "),
                    @ApiResponse(responseCode = "500", description = "Error while deleting todo list.Internal Server Error")
            }
    )
    public HttpStatus deleteUserTodoList(@PathVariable String organizationId,
                                                @PathVariable String userId,
                                                @PathVariable String todoListId) {
        var ok = service.deleteTodoList(organizationId, userId, todoListId);
        if (ok) {
            return HttpStatus.OK;
        }else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

    }
}