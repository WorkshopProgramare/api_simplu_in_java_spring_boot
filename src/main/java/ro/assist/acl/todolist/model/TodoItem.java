package ro.assist.acl.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem {
    private String id;
    private String listId;
    private int placeInList;
    private boolean completed;
    private String content;

}
