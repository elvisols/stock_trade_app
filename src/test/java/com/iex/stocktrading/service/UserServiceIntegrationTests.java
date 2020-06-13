package com.iex.stocktrading.service;

import com.iex.stocktrading.model.dto.NewUserDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class UserServiceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    @Transactional
    public void registerUserTests() throws Exception {

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

}
