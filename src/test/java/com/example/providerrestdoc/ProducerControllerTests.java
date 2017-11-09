package com.example.providerrestdoc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs;
import org.springframework.cloud.contract.wiremock.restdocs.WireMockRestDocs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerControllerTests.Config.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@AutoConfigureJsonTesters
@DirtiesContext
public class ProducerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    private JacksonTester<Person> personJacksonTester;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        personJacksonTester.initFields(this, objectMapper);
    }

    @Test
    public void should_grant_a_beer_when_person_is_old_enough() throws Exception {
        Person person = new Person(34);
        mockMvc.perform(
            MockMvcRequestBuilders.post("/check")
                                  .contentType(MediaType.APPLICATION_JSON)
                                  .content(personJacksonTester.write(person).getJson())
        )
        .andExpect(jsonPath("$.status").value("OK"))
        .andDo(WireMockRestDocs.verify()
                               .jsonPath("$[?(@.age >= 20)]")
                               .contentType(MediaType.valueOf("application/json"))
                               .stub("shouldGrantABeerIfOldEnough")
        )
        .andDo(MockMvcRestDocumentation.document("shouldGrantABeerIfOldEnough",
                                                 SpringCloudContractRestDocs.dslContract())
        );
    }

    @Configuration
    @EnableAutoConfiguration
    static class Config {
        @Bean
        PersonCheckingService personCheckingService() {
            return person -> person.getAge() >= 20;
        }

        @Bean
        ProducerController producerController(PersonCheckingService service) {
            return new ProducerController(service);
        }
    }
}
