package ro.assist.acl.todolist.controller.TodoItemsController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.http11.Http11InputBuffer;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.assist.acl.todolist.Dto.TodoItemsDto;
import ro.assist.acl.todolist.model.TodoItem;
import ro.assist.acl.todolist.service.TodoItemsService;

@CrossOrigin(origins = "*")

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizations/{organizationId}/users/{userId}/todolists")
@Validated
public class TodoItemsController {

    private final TodoItemsService service;

    @PostMapping("/{todoListId}/items")
    @Operation(
            summary = "Create a Todo Item",
            description = "Create a new Todo Item and returns the created Todo Item",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Todo Item created successfully", content = @Content(schema = @Schema(implementation = TodoItem.class))),
                    @ApiResponse(responseCode = "400", description = "Missing required fields in request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, {organizationId,userId} variables don't " +
                            "match the ones from the specified todo list."),
                    @ApiResponse(responseCode = "404", description = "Todo List not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<TodoItem> createTodoListItem(@RequestBody TodoItemsDto todoItemsDto,
                                                       @PathVariable String todoListId,
                                                       @PathVariable String organizationId,
                                                       @PathVariable String userId)
    {
        return new ResponseEntity<>(service.create(organizationId,userId,todoListId,todoItemsDto.getListPlace(),todoItemsDto.isCompleted(),todoItemsDto.getContent()), HttpStatus.CREATED);
    }



    @GetMapping("/{todoListId}/items")
    @Operation(
            summary = "Get Todo List Items",
            description = "Retrieves the items of a Todo List by its Id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todo List items retrieved successfully", content = @Content(schema = @Schema(implementation = Slice.class))),
                    @ApiResponse(responseCode = "400", description = "Missing required fields in request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, {organizationId,userId} variables don't " +
                            "match the ones from the specified todo list."),
                    @ApiResponse(responseCode = "404", description = "Todo List not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public Slice<TodoItemsDto> getTodoListItems(@PathVariable String organizationId,
                                                                @PathVariable String userId,
                                                                @PathVariable String todoListId,
                                                                @ParameterObject @PageableDefault(size = 5, sort = "placeInList")Pageable pageable)
    {
        return service.findAllByListId(organizationId,userId,todoListId, pageable);
    }




    @PutMapping("/{todoListId}/items/{itemId}")
    @Operation(
            summary = "Update a Todo Item",
            description = "Updates a Todo Item by its Id and returns the updated Todo Item",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todo Item updated successfully", content = @Content(schema = @Schema(implementation = TodoItem.class))),
                    @ApiResponse(responseCode = "400", description = "Missing required fields in request"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, {organizationId,userId} variables don't " +
                            "match the ones from the specified todo list."),
                    @ApiResponse(responseCode = "404", description = "Todo List not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public ResponseEntity<TodoItem>  updateTodoItem(@PathVariable String organizationId,
                                                    @PathVariable String userId,
                                                    @PathVariable String todoListId,
                                                    @PathVariable String itemId,
                                                    @RequestBody TodoItemsDto todoItemDto)
    {
        return new ResponseEntity<>(service.update(organizationId,userId,todoListId,itemId,todoItemDto),HttpStatus.OK);
    }



    @DeleteMapping("/{todoListId}/items/{itemId}")
    @Operation(
            summary = "Delete a Todo Item",
            description = "Deletes a Todo Item by its Id and returns no content if it was successfully",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todo Item deleted succesfully"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, {organizationId,userId} variables don't " +
                            "match the ones from the specified todo list."),
                    @ApiResponse(responseCode = "404", description = "Specified todo list for the todo item not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    public HttpStatus deleteTodoItem(@PathVariable String organizationId,
                                            @PathVariable String userId,
                                            @PathVariable String itemId,
                                            @PathVariable String todoListId)
    {
        var ok=service.delete(organizationId,userId,todoListId,itemId);
        if (ok){
            return HttpStatus.OK;
        }else{
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
