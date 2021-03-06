package com.example.demo.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.model.Todo;
import com.example.demo.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Controller
@SessionAttributes("name")
public class TodoController {

	private TodoService service;

	private Logger logger=LoggerFactory.getLogger(TodoController.class);
	public TodoController(TodoService service) {
		super();
		this.service = service;
	}

	/*
	 * @InitBinder public void initBinder(WebDataBinder binder) { // Date -
	 * dd/MM/yyyy SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	 * binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,
	 * false)); }
	 */

	@RequestMapping(value = "/list-todos", method = RequestMethod.GET)
	public String showTodos(ModelMap model) {
		logger.info("within list-todos");
		String name = (String) model.get("name");
		model.put("todos", service.retrieveTodos(name));
		return "list-todos";
	}

	@RequestMapping(value = "/add-todo", method = RequestMethod.GET)
	public String showAddTodoPage(Model model) {
		logger.error("error occured");
		model.addAttribute("todo", new Todo(0, "Default name", "Default Desc", new Date(), false));
		return "todo";
	}

	@RequestMapping(value = "/delete-todo", method = RequestMethod.GET)
	public String deleteTodo(@RequestParam int id) {
		logger.info("within delete todos");
		service.deleteTodo(id);
		return "redirect:/list-todos";
	}

	@RequestMapping(value = "/update-todo", method = RequestMethod.GET)
	public String showUpdateTodoPage(@RequestParam int id, ModelMap model) {
		Todo todo = service.retrieveTodo(id);
		model.put("todo", todo);
		return "todo";
	}

	@RequestMapping(value = "/update-todo", method = RequestMethod.POST)
	public String updateTodo(ModelMap model, @Valid Todo todo, BindingResult result) {

		if (result.hasErrors()) {
			return "todo";
		}

		todo.setUser((String) model.get("name"));

		service.updateTodo(todo);

		return "redirect:/list-todos";
	}

	@RequestMapping(value = "/add-todo", method = RequestMethod.POST)
	public String addTodo(ModelMap model, @Valid Todo todo, BindingResult result) {

		if (result.hasErrors()) {
			return "todo";
		}

		service.addTodo((String) model.get("name"), todo.getDesc(), todo.getTargetDate(), false);
		return "redirect:/list-todos";
	}
}