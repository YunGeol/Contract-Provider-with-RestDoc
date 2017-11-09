package com.example.providerrestdoc;

import org.springframework.stereotype.Service;

@Service
public class PersonCheckingServiceImpl implements PersonCheckingService {

    @Override
    public Boolean shouldGetBeer(Person person) {
        if(person.getAge() > 20) {
            return true;
        } else {
            return false;
        }
    }
}
