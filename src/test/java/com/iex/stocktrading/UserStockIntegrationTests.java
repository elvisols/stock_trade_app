package com.iex.stocktrading;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iex.stocktrading.model.dto.NewUserDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserStockIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Value("${test.jwt.token}")
    private String token;

    @Test
    public void getSymbolsTests() throws Exception {

        NewUserDTO u = getUser();

        this.mvc.perform(get("/user-stocks/symbols")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private NewUserDTO getUser() throws Exception {
        // insert record
        String new_user = "{\n" +
                "    \"full_name\": \"Ben Foe\",\n" +
                "    \"age\": 25,\n" +
                "    \"email\": \"bfoe@example.com\",\n" +
                "    \"username\": \"usertest\",\n" +
                "    \"password\": \"password\",\n" +
                "    \"confirm_password\": \"password\",\n" +
                "    \"account\": \"220122334\"\n" +
                "}";

        MvcResult res = this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new_user)).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        NewUserDTO user = mapper.readValue(res.getResponse().getContentAsString(), NewUserDTO.class);

        return user;
    }

}
