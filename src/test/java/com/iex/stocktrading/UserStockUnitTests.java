package com.iex.stocktrading;

import com.iex.stocktrading.model.EActivity;
import com.iex.stocktrading.model.IEXRecord;
import com.iex.stocktrading.model.Stock;
import com.iex.stocktrading.model.dto.TransactionDTO;
import com.iex.stocktrading.model.dto.UserDTO;
import com.iex.stocktrading.model.dto.UserStockDTO;
import com.iex.stocktrading.service.StockService;
import com.iex.stocktrading.service.UserStockService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserStockUnitTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserStockService usService;

    @MockBean
    private StockService stockService;

    @Value("${test.jwt.token}")
    private String token;

    @BeforeEach
    public void setup() throws Exception {
         getUser();
    }

    @Test
    public void getAllStocksByUserTest() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("elviso@aet.com");
        userDTO.setAccount("391233322");
        userDTO.setAge(21);
        userDTO.setBalance(BigDecimal.valueOf(942.35));

        Stock stock = new Stock();
        stock.setSymbol("AA");
        stock.setExchange("NYS");
        stock.setName("Alcoa Corp.");

        UserStockDTO userStockDTO = new UserStockDTO();
        userStockDTO.setId(1L);
        userStockDTO.setShares(10);
        userStockDTO.setCurrent_price(BigDecimal.valueOf(11.2));
        userStockDTO.setUser(userDTO);
        userStockDTO.setStock(stock);

        UserStockDTO userStockDTO2 = new UserStockDTO();
        userStockDTO2.setId(2L);
        userStockDTO2.setShares(16);
        userStockDTO2.setCurrent_price(BigDecimal.valueOf(43.1));
        userStockDTO2.setUser(userDTO);
        userStockDTO2.setStock(stock);

        given(this.usService.findAllByUser(any(Pageable.class))).willReturn(new PageImpl<>(Arrays.asList(userStockDTO, userStockDTO2)));

        this.mvc.perform(get("/user-stocks").param("size", "20").param("page", "0")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload", Matchers.hasSize(2)))
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("size").description("Size of the returned payload").optional(),
                                        parameterWithName("page").description("Payload page number").optional()
                                ),
                                responseFields(
                                        fieldWithPath("payload[].id").description("User stock auto generated ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("payload[].user.id").description("Registered user ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("payload[].user.email").description("Registered user email").type(JsonFieldType.STRING),
                                        fieldWithPath("payload[].user.age").description("Registered age").type(JsonFieldType.NUMBER),
                                        fieldWithPath("payload[].user.account").description("Registered user account").type(JsonFieldType.STRING),
                                        fieldWithPath("payload[].user.balance").description("Registered user balance").type(JsonFieldType.NUMBER),
                                        fieldWithPath("payload[].stock.symbol").description("Registered stock symbol").type(JsonFieldType.STRING),
                                        fieldWithPath("payload[].stock.exchange").description("Registered stock exchange acronym").type(JsonFieldType.STRING),
                                        fieldWithPath("payload[].stock.name").description("Registered stock name").type(JsonFieldType.STRING),
                                        fieldWithPath("payload[].shares").description("User stock shares available").type(JsonFieldType.NUMBER),
                                        fieldWithPath("payload[].current_price").description("Stock current price").type(JsonFieldType.NUMBER)
                                )
                        )
                );

        verify(usService, times(1)).findAllByUser(any(Pageable.class));
    }

    @Test
    public void getCurrentStockPriceTest() throws Exception {

        IEXRecord iexRecord = new IEXRecord();
        iexRecord.setSymbol("NFLX");
        iexRecord.setCompanyName("Netflix, Inc.");
        iexRecord.setPrimaryExchange("NASDAQ");
        iexRecord.setOpen(429.0);
        iexRecord.setClose(418.2);
        iexRecord.setHigh(434.06);
        iexRecord.setLow(412.45);
        iexRecord.setLatestPrice(418.07);
        iexRecord.setLatestTime("June 12, 2020");
        iexRecord.setLatestVolume(6461127L);
        iexRecord.setMarketCap(183868858280L);

        given(this.stockService.findOne(anyString())).willReturn(iexRecord);

        this.mvc.perform(get("/user-stocks/symbol/{stock_symbol}", "NFLX")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol", Matchers.is("NFLX")))
                .andExpect(jsonPath("$.latestPrice", Matchers.is(418.07)))
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("stock_symbol").description("Stock symbol")
                                ),
                                responseFields(
                                        fieldWithPath("symbol").description("IEX stock symbol").type(JsonFieldType.STRING),
                                        fieldWithPath("companyName").description("Registered company name").type(JsonFieldType.STRING),
                                        fieldWithPath("primaryExchange").description("Registered primary exchange").type(JsonFieldType.STRING),
                                        fieldWithPath("open").description("Stock open price").type(JsonFieldType.NUMBER),
                                        fieldWithPath("close").description("Stock close price").type(JsonFieldType.NUMBER),
                                        fieldWithPath("high").description("Stock high price").type(JsonFieldType.NUMBER),
                                        fieldWithPath("low").description("Stock low price").type(JsonFieldType.NUMBER),
                                        fieldWithPath("latestPrice").description("Stock latest price").type(JsonFieldType.NUMBER),
                                        fieldWithPath("latestTime").description("Stock latest time").type(JsonFieldType.STRING),
                                        fieldWithPath("latestVolume").description("Stock latest volume").type(JsonFieldType.NUMBER),
                                        fieldWithPath("marketCap").description("Stock market cap").type(JsonFieldType.NUMBER)
                                )
                        )
                );

        verify(stockService, times(1)).findOne(anyString());
    }

    @Test
    public void buyStockTest() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("elviso@aet.com");
        userDTO.setAccount("391233322");
        userDTO.setAge(21);
        userDTO.setBalance(BigDecimal.valueOf(942.35));

        Stock stock = new Stock();
        stock.setSymbol("AA");
        stock.setExchange("NYS");
        stock.setName("Alcoa Corp.");

        UserStockDTO userStockDTO = new UserStockDTO();
        userStockDTO.setId(1L);
        userStockDTO.setShares(10);
        userStockDTO.setCurrent_price(BigDecimal.valueOf(11.2));
        userStockDTO.setUser(userDTO);
        userStockDTO.setStock(stock);

        String shares = "{\n" +
                "    \"shares\": 3\n" +
                "}";

        given(this.usService.buy(anyString(), anyInt())).willReturn(Optional.of(userStockDTO));

        this.mvc.perform(post("/user-stocks/buy/{stock_symbol}", "AA")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shares)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("stock_symbol").description("Stock symbol to buy")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("User stock auto generated ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("user.id").description("Registered user ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("user.email").description("Registered user email").type(JsonFieldType.STRING),
                                        fieldWithPath("user.age").description("Registered age").type(JsonFieldType.NUMBER),
                                        fieldWithPath("user.account").description("Registered user account").type(JsonFieldType.STRING),
                                        fieldWithPath("user.balance").description("Registered user balance").type(JsonFieldType.NUMBER),
                                        fieldWithPath("stock.symbol").description("Registered stock symbol").type(JsonFieldType.STRING),
                                        fieldWithPath("stock.exchange").description("Registered stock exchange acronym").type(JsonFieldType.STRING),
                                        fieldWithPath("stock.name").description("Registered stock name").type(JsonFieldType.STRING),
                                        fieldWithPath("shares").description("User stock shares available").type(JsonFieldType.NUMBER),
                                        fieldWithPath("current_price").description("Stock current price").type(JsonFieldType.NUMBER)
                                )
                        )
                );

        verify(usService, times(1)).buy(anyString(), anyInt());
    }

    @Test
    public void sellStockTest() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("elviso@example.com");
        userDTO.setAccount("391233322");
        userDTO.setAge(22);
        userDTO.setBalance(BigDecimal.valueOf(942.35));

        Stock stock = new Stock();
        stock.setSymbol("AA");
        stock.setExchange("NYS");
        stock.setName("Alcoa Corp.");

        UserStockDTO userStockDTO = new UserStockDTO();
        userStockDTO.setId(1L);
        userStockDTO.setShares(10);
        userStockDTO.setCurrent_price(BigDecimal.valueOf(11.2));
        userStockDTO.setUser(userDTO);
        userStockDTO.setStock(stock);

        String shares = "{\n" +
                "    \"shares\": 3\n" +
                "}";

        given(this.usService.sell(anyString(), anyInt())).willReturn(Optional.of(userStockDTO));

        this.mvc.perform(post("/user-stocks/sell/{stock_symbol}", "AA")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(shares)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(
                                        parameterWithName("stock_symbol").description("Stock symbol to buy")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("User stock auto generated ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("user.id").description("Registered user ID").type(JsonFieldType.NUMBER),
                                        fieldWithPath("user.email").description("Registered user email").type(JsonFieldType.STRING),
                                        fieldWithPath("user.age").description("Registered age").type(JsonFieldType.NUMBER),
                                        fieldWithPath("user.account").description("Registered user account").type(JsonFieldType.STRING),
                                        fieldWithPath("user.balance").description("Registered user balance").type(JsonFieldType.NUMBER),
                                        fieldWithPath("stock.symbol").description("Registered stock symbol").type(JsonFieldType.STRING),
                                        fieldWithPath("stock.exchange").description("Registered stock exchange acronym").type(JsonFieldType.STRING),
                                        fieldWithPath("stock.name").description("Registered stock name").type(JsonFieldType.STRING),
                                        fieldWithPath("shares").description("User stock shares available").type(JsonFieldType.NUMBER),
                                        fieldWithPath("current_price").description("Stock current price").type(JsonFieldType.NUMBER)
                                )
                        )
                );

        verify(usService, times(1)).sell(anyString(), anyInt());
    }


    @Test
    public void getTransactionSummaryTest() throws Exception {

        TransactionDTO transaction = new TransactionDTO();
        transaction.setShares(23);
        transaction.setActivity(EActivity.buy.toString());
        transaction.setAmount(250.1);
        transaction.setTimestamp(new Date());
        transaction.setUser("Uzer");
        transaction.setStock("AAPL");

        given(this.usService.getTransactionSummary(any(), any(), any(), any(Pageable.class))).willReturn(new PageImpl<>(Arrays.asList(transaction)));

        this.mvc.perform(get("/user-stocks/transactions")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].stock", Matchers.is("AAPL")))
                .andExpect(jsonPath("$.content[0].shares", Matchers.is(23)))
                .andDo(
                        document("{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("from").description("Filter the transaction records from a specific date... [yyyy-MM-dd]. Defaults to 3 days ago").optional(),
                                        parameterWithName("to").description("Filter the transaction records to a specific date... [yyyy-MM-dd]. Defaults to now.").optional(),
                                        parameterWithName("all").description("Filter the transactions by activities performed... Enum{buy|sell|all}. Defaults to all").optional()
                                ),
                                responseFields(
                                        fieldWithPath("content[].shares").description("Shares count sold or bought").type(JsonFieldType.NUMBER),
                                        fieldWithPath("content[].activity").description("Activity recorded... Enum{buy|sell|all}").type(JsonFieldType.STRING),
                                        fieldWithPath("content[].amount").description("Transaction amount").type(JsonFieldType.NUMBER),
                                        fieldWithPath("content[].timestamp").description("Transaction timestamp").type(JsonFieldType.STRING),
                                        fieldWithPath("content[].user").description("User buyer or seller").type(JsonFieldType.STRING),
                                        fieldWithPath("content[].stock").description("Stock sold or purchased").type(JsonFieldType.STRING),
                                        fieldWithPath("pageable").description("Pageable instance").type(JsonFieldType.STRING),
                                        fieldWithPath("last").description("Last record view").type(JsonFieldType.BOOLEAN),
                                        fieldWithPath("totalPages").description("Page total").type(JsonFieldType.NUMBER),
                                        fieldWithPath("totalElements").description("Total Element").type(JsonFieldType.NUMBER),
                                        fieldWithPath("size").description("Page size").type(JsonFieldType.NUMBER),
                                        fieldWithPath("number").description("Number count").type(JsonFieldType.NUMBER),
                                        fieldWithPath("numberOfElements").description("Number of Element").type(JsonFieldType.NUMBER),
                                        fieldWithPath("first").description("First record view").type(JsonFieldType.BOOLEAN),
                                        fieldWithPath("sort.sorted").description("Sorted record").type(JsonFieldType.BOOLEAN),
                                        fieldWithPath("sort.unsorted").description("Unsorted record").type(JsonFieldType.BOOLEAN),
                                        fieldWithPath("sort.empty").description("Empty record").type(JsonFieldType.BOOLEAN),
                                        fieldWithPath("empty").description("Is record Empty").type(JsonFieldType.BOOLEAN)
                                )
                        )
                );

        verify(usService, times(1)).getTransactionSummary(any(), any(), any(), any());
    }


    private void getUser() throws Exception {
        // insert record
        String new_user = "{\n" +
                "    \"full_name\": \"Ben Foe2\",\n" +
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
