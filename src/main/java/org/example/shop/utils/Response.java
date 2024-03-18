package org.example.shop.utils;

import lombok.Data;

@Data
public class Response <T>{
    private  int count;
    private  T data;
    private  String message;
    public Response(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
