package com.example.wegrow.model;

import java.util.ArrayList;

/**
 * User
 */
public class User {
    private String j ;
    User(String j ){
        this.j = j;
    }
    public String getJ() {
        return j;
    }
    
    public static void main(String[] args) {
        System.out.println( new User("Hello World").getJ());
        System.out.println("Hello World");
       var ArrayOfStrings =  getList();
       System.out.println(ArrayOfStrings);
    }
    public static ArrayList<String> getList(){
        ArrayList<String> listOf= new ArrayList<>();
        listOf.add("hahaha");
        listOf.add("Hello");
       return listOf;
    }
}
