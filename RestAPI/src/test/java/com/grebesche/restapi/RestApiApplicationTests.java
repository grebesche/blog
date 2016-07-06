package com.grebesche.restapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grebesche.restapi.db.Task;
import com.grebesche.restapi.db.TaskRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestApiApplication.class)
@WebAppConfiguration
public class RestApiApplicationTests {

  @Rule
  public RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

  @Autowired
  private WebApplicationContext context;
  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private ObjectMapper objectMapper;

  private MockMvc mvc;
  private RestDocumentationResultHandler document;

  @Before
  public void before() {
    this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    this.document = MockMvcRestDocumentation.document("{method-name}", Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));
    this.mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation)
            .uris()
            .withScheme("https")
            .withHost("onedesk.com")
            .withPort(80))
        .alwaysDo(this.document)
        .build();
  }

  @After
  public void after() {
    taskRepository.deleteAll();
  }

  @Test
  public void testCreateTask() throws Exception {
    String name = "My task";
    LocalDateTime start = LocalDateTime.now();
    LocalDateTime finish = LocalDateTime.now().plusDays(2);

    Task task = new Task();
    task.setName(name);
    task.setStart(start);
    task.setFinish(finish);

    this.document.snippets(
        requestFields(
            fieldWithPath("name")
                .type(JsonFieldType.STRING)
                .description("Authentication token"),
            fieldWithPath("start")
                .type(JsonFieldType.STRING)
                .description("The new user email"),
            fieldWithPath("finish")
                .type(JsonFieldType.STRING)
                .description("true if the new user should be an administrator")
        )
        /*responseFields(
            fieldWithPath("code")
                .type(JsonFieldType.STRING)
                .description("the result of the API operation"),
            fieldWithPath("data.id")
                .type(JsonFieldType.NUMBER)
                .description("Id of the newly created user")
        )*/
    );

    mvc.perform(post("/task")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(task)))
        .andExpect(status().isOk());

    List<Task> all = taskRepository.findAll();
    assertEquals(1, all.size());
    Task actual = all.get(0);
    assertNotNull(actual.getId());
    assertEquals(name, actual.getName());
    assertEquals(start, actual.getStart());
    assertEquals(finish, actual.getFinish());
  }

  @Test
  public void testGetTask() throws Exception {
    Task task = new Task();
    task.setName("My task");
    task.setStart(LocalDateTime.now());
    task.setFinish(LocalDateTime.now().plusDays(2));
    taskRepository.save(task);

    mvc.perform(get("/task/" + task.getId()))
        .andExpect(content().string(objectMapper.writeValueAsString(task)))
        .andExpect(status().isOk());
  }

  @Test
  public void testUpdateTask() throws Exception {
    Task task = new Task();
    task.setName("My task");
    task.setStart(LocalDateTime.now());
    task.setFinish(LocalDateTime.now().plusDays(2));
    taskRepository.save(task);

    String updatedName = "My task";
    task.setName(updatedName);

    mvc.perform(put("/task")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(task)))
        .andExpect(status().isOk());

    Task updated = taskRepository.findOne(task.getId());
    assertEquals(updatedName, updated.getName());
  }

  @Test
  public void testDeleteTask() throws Exception {
    Task task = new Task();
    task.setName("My task");
    task.setStart(LocalDateTime.now());
    task.setFinish(LocalDateTime.now().plusDays(2));
    taskRepository.save(task);

    mvc.perform(delete("/task/" + task.getId()))
        .andExpect(status().isOk());

    List<Task> all = taskRepository.findAll();
    assertEquals(0, all.size());
  }
}
