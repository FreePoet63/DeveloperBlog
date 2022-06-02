package com.razlivinskiyproject.developerBlogTest;

import com.razlivinskiyproject.developerBlog.DeveloperBlogApplication;
import com.razlivinskiyproject.developerBlog.controllers.BlogController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DeveloperBlogApplication.class})
@AutoConfigureMockMvc
@WithUserDetails(value = "Natasha")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/blog-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/blog-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BlogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogController blogController;

    @Test
    public void blogPageTest() throws Exception {
        this.mockMvc.perform(get("/blog"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//a[text()='Natasha']").string("Natasha"));
    }

    @Test
    public void blogListTest() throws Exception {
        this.mockMvc.perform(get("/blog"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[starts-with(@class, 'alert')]").nodeCount(3));
    }

    @Test
    public void blogTitleList() throws Exception {
        this.mockMvc.perform(get("/blog"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[starts-with(@class, 'alert')]/h3").nodeCount(3))
                .andExpect(xpath("//div[starts-with(@class, 'alert')][1]/h3").string("Любовь"))
                .andExpect(xpath("//div[starts-with(@class, 'alert')][2]/h3").string("Жизнь"))
                .andExpect(xpath("//div[starts-with(@class, 'alert')][3]/h3").string("Футбол"));
    }

    @Test
    public void addPostToBlog() throws Exception {
        MockHttpServletRequestBuilder builder = multipart("/blog/add")
                .param("title", "Мама")
                .param("anons", "О любимой маме")
                .param("text", "Как я люблю мамулечку")
                .with(csrf());
        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog"));
        this.mockMvc.perform(get("/blog"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[starts-with(@class, 'alert')]").nodeCount(4))
                .andExpect(xpath("//div[starts-with(@class, 'alert')][4]/h3").string("Мама"))
                .andExpect(xpath("//div[starts-with(@class, 'alert')][4]/p[1]").string("О любимой маме"));
        this.mockMvc.perform(get("/blog/10"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//h1").string("Мама"))
                .andExpect(xpath("//div[starts-with(@class, 'alert')]/p[1]").string("Как я люблю мамулечку"));
    }

    @Test
    public void editPostBlog() throws Exception {
        MockHttpServletRequestBuilder builder = multipart("/blog/1/edit")
                .param("title", "Любовь!!!")
                .param("anons", "О любви!!!")
                .param("text", "Я очень люблю этот мир!!!")
                .with(csrf());
        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog"));
        this.mockMvc.perform(get("/blog/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//h1").string("Любовь!!!"))
                .andExpect(xpath("//div[starts-with(@class, 'alert')]/p[1]").string("Я очень люблю этот мир!!!"));
    }
}

