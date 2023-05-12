package ro.assist.acl.todolist.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTodoListDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @NotNull
    private String title;
}