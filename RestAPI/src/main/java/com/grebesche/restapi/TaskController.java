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
public class TaskController {

  private TaskRepository taskRepository;

  @Autowired
  public TaskController(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @RequestMapping(value = "/task/{taskId}", method = RequestMethod.GET)
  @ResponseBody
  public Task getTask(@PathVariable Long taskId) {
    return taskRepository.findOne(taskId);
  }

  @RequestMapping(value = "/task", method = RequestMethod.POST)
  @ResponseBody
  public Long createTask(@RequestBody Task task) {
    return taskRepository.save(task).getId();
  }

  @RequestMapping(value = "/task", method = RequestMethod.PUT)
  @ResponseBody
  public void updateTask(@RequestBody Task task) {
    taskRepository.save(task);
  }

  @RequestMapping(value = "/task/{taskId}", method = RequestMethod.DELETE)
  @ResponseBody
  public void deleteTask(@PathVariable Long taskId) {
    taskRepository.delete(taskId);
  }
}
