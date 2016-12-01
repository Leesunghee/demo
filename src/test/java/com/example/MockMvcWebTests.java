package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by hds on 2016. 12. 1..
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReadingListApplication.class)
@WebAppConfiguration //웹 컨텍스트 테스트 활성화
public class MockMvcWebTests {

    @Autowired
    private WebApplicationContext webApplicationContext; //WebApplicationContext injection

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build(); // Moc
    }

    @Test
    public void homePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", is(empty())));
    }

    @Test
    public void postBook() throws Exception {
        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "BOOK TITLE")
                .param("author", "BOOK_AUTHOR")
                .param("isbn", "12345465")
                .param("description", "DESCRIPTION"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/"));

        Reader reader = new Reader();
        reader.setUsername("lee");
        Book expectedBook = new Book();
        expectedBook.setId(1L);
        expectedBook.setReader(reader);
        expectedBook.setTitle("BOOK TITLE");
        expectedBook.setAuthor("BOOK AUTHOR");
        expectedBook.setIsbn("12345465");
        expectedBook.setDescription("DESCRIPTION");

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", hasSize(1)))
                .andExpect(model().attribute("books", contains(samePropertyValuesAs(expectedBook))));


    }

    @Test
    public void homePage_unauthenticatedUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @Test
    @WithUserDetails("lee")
    public void homePage_authenticatedUser() throws Exception {
        Reader expectedReader = new Reader();
        expectedReader.setUsername("lee");
        expectedReader.setPassword("password");
        expectedReader.setFullname("Craig Walls");

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("readingList"))
                .andExpect(model().attribute("reader", samePropertyValuesAs(expectedReader)))
                .andExpect(model().attribute("books", hasSize(0)))
                .andExpect(model().attribute("amazonID", "habuam-20"));
    }
}