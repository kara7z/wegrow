package com.example.wegrow.controller;

/**
 * AuthController
 */
public class AuthController {
    private int id =0; 
    private String j = " h";
    private int nextId;
    
    AuthController(String j){
        this.j = j;
        this.id = ++nextId;
    }
    public int getId() {
        return id;
    }
    public int getNextId() {
        return nextId;
    }
}