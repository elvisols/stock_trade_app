package com.iex.stocktrading;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.UserDTO;
import org.hamcrest.Matchers;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

    @Test
    public void getSymbolsTests() throws Exception {

        String user = "{\n" +
                "    \"full_name\": \"Ben Foe\",\n" +
                "    \"age\": 25,\n" +
                "    \"email\": \"bfoe@example.com\",\n" +
                "    \"username\": \"boe\",\n" +
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
    public void updateUserTests() throws Exception {

        UserDTO u = getUser();

        String user = "{\n" +
                "    \"full_name\": \"Ben Updated\",\n" +
                "    \"age\": 30,\n" +
                "    \"email\": \"bfoe2@example.com\",\n" +
                "    \"username\": \"boe\",\n" +
                "    \"password\": \"passiton\",\n" +
                "    \"confirm_password\": \"passiton\",\n" +
                "    \"account\": \"220122334\"\n" +
                "}";

        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(NewUserDTO.class);

        this.mvc.perform(put("/users/" + u.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(user)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name", Matchers.is("Ben Updated")))
                .andExpect(jsonPath("$.email", Matchers.is("bfoe2@example.com")))
                .andExpect(jsonPath("$.age", Matchers.is(30)))
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

    private UserDTO getUser() throws Exception {
        // insert record
        String new_user = "{\n" +
                "    \"full_name\": \"Ben Foe\",\n" +
                "    \"age\": 25,\n" +
                "    \"email\": \"bfoe@example.com\",\n" +
                "    \"username\": \"boe\",\n" +
                "    \"password\": \"passiton\",\n" +
                "    \"confirm_password\": \"passiton\",\n" +
                "    \"account\": \"220122334\"\n" +
                "}";

        MvcResult res = this.mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new_user)).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        UserDTO user = mapper.readValue(res.getResponse().getContentAsString(), UserDTO.class);

        return user;
    }

}
