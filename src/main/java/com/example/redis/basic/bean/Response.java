package com.example.redis.basic.bean;

import lombok.Data;

@Data
public class Response<T> {

	String status;
	String message;
	T data;
	
}
