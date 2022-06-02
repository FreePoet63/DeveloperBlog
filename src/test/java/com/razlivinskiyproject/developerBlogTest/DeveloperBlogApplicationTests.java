package com.razlivinskiyproject.developerBlogTest;

import com.razlivinskiyproject.developerBlog.DeveloperBlogApplication;
import com.razlivinskiyproject.developerBlog.controllers.MainController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DeveloperBlogApplication.class})
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class DeveloperBlogApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MainController mainController;

	@Test
	void contextLoads() throws Exception {
		this.mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Моя Любимая Мама")));
	}

	@Test
	void loginTest() throws Exception {
		this.mockMvc.perform(get("/blog"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));
	}

	@Test
	@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	void correctLogin() throws Exception {
		this.mockMvc.perform(formLogin().user("Natasha").password("1963"))
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/"));
	}

	@Test
	void incorrectLogin () throws Exception {
		this.mockMvc.perform(post("/login").param("user", "David"))
				.andDo(print())
				.andExpect(status().isForbidden());
	}

}
