package com.example.wegrow.controller;

/**
 * AuthController
 */
public class AuthController {
    private int id ;
    private int nextId = 0;
    
    AuthController(){
        id = ++nextId;
    }

    public int getId() {
        return id;
    }
    public int getNextId() {
        return nextId;
    }
}