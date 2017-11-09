package com.example.providerrestdoc;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    private PersonCheckingService personCheckingService;

    public ProducerController(PersonCheckingService personCheckingService) {
        this.personCheckingService = personCheckingService;
    }

    @RequestMapping(value = "/check",
                    method = RequestMethod.POST)
    public Response check(@RequestBody Person person) {
        if (personCheckingService.shouldGetBeer(person)) {
            return new Response(BeerCheckStatus.OK);
        }
        return new Response(BeerCheckStatus.NOT_OK);
    }
}
