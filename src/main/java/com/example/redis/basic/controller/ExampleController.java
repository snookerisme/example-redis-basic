package com.example.redis.basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.redis.basic.bean.Response;
import com.example.redis.basic.service.ExampleService;

@RestController
public class ExampleController {
	
	@Autowired
	private ExampleService service;

	@PostMapping(value = "/load")
	public ResponseEntity<Response<?>> loadData() {
		Response<?> response = service.readFileAndPushToRedis();
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/get/{id}")
	public ResponseEntity<Response<String>> gettData(@PathVariable("id") String id) {
		Response<String> response = service.getValueByIdFromRedis(id);
		return ResponseEntity.ok(response);
	}
}
