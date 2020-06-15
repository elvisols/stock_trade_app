package com.iex.stocktrading;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iex.stocktrading.model.EActivity;
import com.iex.stocktrading.model.Stock;
import com.iex.stocktrading.model.Transaction;
import com.iex.stocktrading.model.User;
import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.TransactionDTO;
import com.iex.stocktrading.model.dto.UserDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserUnitTests {

    @Autowired
    private MockMvc mvc;

    @Value("${test.jwt.token}")
    private String token;

    @BeforeEach
    public void setup() throws Exception {
        getUser();
    }

    @Test
    public void registerUserTests() throws Exception {

        String user = "{\n" +
                "    \"full_name\": \"John Foe\",\n" +
                "    \"age\": 25,\n" +
                "    \"email\": \"bfoe@example.com\",\n" +
                "    \"username\": \"jerryben\",\n" +
                "    \"password\": \"passiton\",\n" +
                "    \"confirm_password\": \"passiton\",\n" +
                "    \"account\": \"220122334\"\n" +
                "}";

        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(NewUserDTO.class);

        this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("full_name").description("User full name")
                                                .type(JsonFieldType.STRING)
                                                .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("full_name"))),
                                        fieldWithPath("email").description("User email address")
                                                .type(JsonFieldType.STRING)
                                                .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("email"))),
                                        fieldWithPath("account").description("User account")
                                                .type(JsonFieldType.STRING)
                                                .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("account"))),
                                        fieldWithPath("username").description("User login username")
                                                .type(JsonFieldType.STRING)
                                                .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("username"))),
                                        fieldWithPath("password").description("User login password")
                                                .type(JsonFieldType.STRING)
                                                .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("password"))),
                                        fieldWithPath("confirm_password").description("User login confirmation password")
                                                .type(JsonFieldType.STRING)
                                                .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("confirm_password"))),
                                        fieldWithPath("age").description("User age")
                                                .type(JsonFieldType.NUMBER)
                                                .optional()
                                                .attributes(key("constraints").value(constraintDescriptions.descriptionsForProperty("age")))
                                ),
                                responseFields(
                                        fieldWithPath("id").description("User auto generated ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("age").description("Registered user age").type(JsonFieldType.NUMBER),
                                        fieldWithPath("full_name").description("Registered user full name").type(JsonFieldType.STRING),
                                        fieldWithPath("email").description("Registered person age").type(JsonFieldType.STRING),
                                        fieldWithPath("account").description("Registered user account").type(JsonFieldType.STRING),
                                        fieldWithPath("balance").description("Registered user balance").type(JsonFieldType.NUMBER)
                                )
                        )
                );
    }


    @Test
    public void fundAccountTests() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("elviso@example.com");
        userDTO.setAccount("391233322");
        userDTO.setAge(21);
        userDTO.setBalance(BigDecimal.valueOf(1000));

        this.mvc.perform(post("/users/fund-account/{amount}", 1000)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", Matchers.is(1000)))
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("amount").description("Amount to credit account.")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("User auto generated ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("full_name").description("Registered user full name").type(JsonFieldType.STRING),
                                        fieldWithPath("email").description("Registered user email").type(JsonFieldType.STRING),
                                        fieldWithPath("age").description("Registered user age").type(JsonFieldType.NUMBER),
                                        fieldWithPath("account").description("Registered user account").type(JsonFieldType.STRING),
                                        fieldWithPath("balance").description("Registered user balance").type(JsonFieldType.NUMBER)
                                )
                        )
                );

    }


    @Test
    public void getSymbolsTests() throws Exception {

        this.mvc.perform(get("/user-stocks/symbols")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void loginTest() throws Exception {

        String loginRequest = "{\n" +
                "    \"username\": \"usertest\",\n" +
                "    \"password\": \"password\"\n" +
                "}";

        this.mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", Matchers.isA(Boolean.class)))
                .andExpect(jsonPath("$.token", Matchers.startsWith("Bearer")))
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("username").description("Login Username")
                                                .type(JsonFieldType.STRING)
                                                .attributes(key("constraints").value("Username cannot be blank")),
                                        fieldWithPath("password").description("Login password")
                                                .type(JsonFieldType.STRING)
                                                .attributes(key("constraints").value("Password cannot be blank"))
                                ),
                                responseFields(
                                        fieldWithPath("status").description("Valid token status").type(JsonFieldType.BOOLEAN),
                                        fieldWithPath("token").description("JWT token value").type(JsonFieldType.STRING)
                                )
                        )
                );
    }

    @Test
    public void loginFailTest() throws Exception {

        String loginRequest = "{\n" +
                "    \"username\": \"usertest\",\n" +
                "    \"password\": \"passw@rd\"\n" +
                "}";

        this.mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private void getUser() throws Exception {
        // insert record
        String new_user = "{\n" +
                "    \"full_name\": \"Ben John\",\n" +
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

    }

}
