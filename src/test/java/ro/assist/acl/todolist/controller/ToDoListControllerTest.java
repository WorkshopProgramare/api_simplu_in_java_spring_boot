//package ro.assist.acl.todolist.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.SliceImpl;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import ro.assist.acl.todolist.common.CustomPage;
//import ro.assist.acl.todolist.Dto.UserTodoListDto;
//import ro.assist.acl.todolist.service.ToDoListService;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(TodoListController.class)
//@ContextConfiguration(classes = TodoListController.class)
//@ExtendWith(MockitoExtension.class)
//class ToDoListControllerTest {
//    @MockBean
//    private ToDoListService toDoListService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void getUserTodoLists() throws Exception {
//        var userTodoListDtos = List.of(new UserTodoListDto("id", "test"));
//
//        doReturn(new CustomPage<>(new SliceImpl<>(userTodoListDtos)))
//                .when(toDoListService).todoLists(any(), any(), any());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/organizations/testOrg/users/testUser/todolists"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.toDoLists", hasSize(1)));
//    }
//
//    @Test
//    void addUserTodoList_shouldSucceed() throws Exception {
//
//        doNothing().when(toDoListService).addTodoList(any(), any(), any());
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/organizations/testOrg/users/testUser/todolists")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(new UserTodoListDto("testId", "testTitle")))
//                )
//                .andExpect(status().isOk());
//
//        var orgIdCaptor = ArgumentCaptor.forClass(String.class);
//        var userIdCaptor = ArgumentCaptor.forClass(String.class);
//        var dtoCaptor = ArgumentCaptor.forClass(UserTodoListDto.class);
//        verify(toDoListService).addTodoList(orgIdCaptor.capture(), userIdCaptor.capture(), dtoCaptor.capture());
//
//        var orgId = orgIdCaptor.getValue();
//        var userId = userIdCaptor.getValue();
//        var dto = dtoCaptor.getValue();
//        assertNotNull(orgId);
//        assertEquals("testOrg", orgId);
//        assertNotNull(userId);
//        assertEquals("testUser", userId);
//        assertNotNull(dto);
//        assertNull(dto.getId());
//        assertEquals("testTitle", dto.getTitle());
//    }
//    @Test
//    void addUserTodoList_shouldFail_when_title_isNull() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/organizations/testOrg/users/testUser/todolists")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(new UserTodoListDto("testId", null)))
//                )
//                .andExpect(status().is4xxClientError());
//    }
//}