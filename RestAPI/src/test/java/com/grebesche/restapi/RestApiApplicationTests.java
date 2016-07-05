package com.grebesche.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grebesche.restapi.db.Task;
import com.grebesche.restapi.db.TaskRepository;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestApiApplication.class)
@WebAppConfiguration
public class RestApiApplicationTests {

  @Autowired
  private WebApplicationContext context;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private TaskRepository taskRepository;

  private MockMvc mvc;

  @Before
  public void before() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
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

    mvc.perform(post("/task")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(task)));

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
        .andExpect(content().string(objectMapper.writeValueAsString(task)));
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
        .content(objectMapper.writeValueAsString(task)));

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

    mvc.perform(delete("/task/" + task.getId()));

    List<Task> all = taskRepository.findAll();
    assertEquals(0, all.size());
  }
}
