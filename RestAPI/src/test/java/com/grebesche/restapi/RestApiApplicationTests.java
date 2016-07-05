package com.grebesche.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grebesche.restapi.db.Task;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestApiApplication.class)
@WebAppConfiguration
public class RestApiApplicationTests {

  @Autowired
  private WebApplicationContext context;
  @Autowired
  private ObjectMapper objectMapper;

  private MockMvc mvc;

  @Before
  public void before() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .build();
  }

  @Test
  public void createTask() throws Exception {
    String name = "My task";
    Date start = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    Date finish = Date.from(LocalDateTime.now().plusDays(2).atZone(ZoneId.systemDefault()).toInstant());

    Task task = new Task();
    task.setName(name);
    task.setStart(start);
    task.setFinish(finish);

    Task returnTask = new Task();
    returnTask.setId(1L);
    returnTask.setName(name);
    returnTask.setStart(start);
    returnTask.setFinish(finish);

    mvc.perform(post("/task")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(task)))
        .andExpect(content().string(objectMapper.writeValueAsString(returnTask)));
  }

}
