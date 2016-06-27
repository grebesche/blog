package com.grebesche.restapi;

import com.grebesche.restapi.db.Task;
import com.grebesche.restapi.db.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

  private TaskRepository taskRepository;

  @Autowired
  public TaskController(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
  @ResponseBody
  public Task getTask(@PathVariable Long taskId) {
    return taskRepository.findOne(taskId);
  }

  @RequestMapping(value = "/", method = RequestMethod.POST)
  @ResponseBody
  public Task createTask(@RequestBody Task task) {
    return taskRepository.save(task);
  }
}
