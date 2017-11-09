package com.example.providerrestdoc;

import lombok.Data;

@Data
public class Person {
    private int age;
    public Person(int age) {
        this.age = age;
    }
    public Person() {

    }
}
