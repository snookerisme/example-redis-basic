package com.example.redis.basic.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.redis.basic.bean.Response;
import com.example.redis.basic.constant.ApplicationConstants.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExampleService {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public Response<?> readFileAndPushToRedis() {
		Response<?> response = new Response<>();
		try {
			File myObj = new File("data.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String key = data.split("\\|")[0];
				String value = data.split("\\|")[1];
				ObjectMapper mapper = new ObjectMapper();
				Map<String, String> map = mapper.readValue(value,Map.class);
				redisTemplate.opsForHash().putAll(key, map);
			}
			myReader.close();
			response.setStatus(Status.SUCCESS);
		}catch (JsonMappingException ex) {
			response.setStatus(Status.ERROR);
			response.setMessage(ex.getMessage());
		}catch (JsonProcessingException ex2) {
			response.setStatus(Status.ERROR);
			response.setMessage(ex2.getMessage());
		}catch (FileNotFoundException ex3) {
			response.setStatus(Status.ERROR);
			response.setMessage(ex3.getMessage());
		}
		return response;
	}
	
	public Response<String> getValueByIdFromRedis(String id) {
		log.info("{}",id);
		Response<String> response = new Response<String>();
		Map<Object, Object> value = redisTemplate.opsForHash().entries("key:" + id);
		if (!(value.isEmpty())) {
			response.setStatus(Status.SUCCESS);
			response.setData(value.get("value").toString());
		}else {
			response.setStatus(Status.ERROR);
			response.setMessage("Data not found : " + id);
		}
		return response;
	}
	
}
