package com.nanal.backend.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanal.backend.global.security.User;
import com.nanal.backend.global.security.jwt.JwtAuthFilter;
import com.nanal.backend.global.security.jwt.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class CommonControllerTest {

    public MockMvc mockMvc;
    public ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    public TokenUtil tokenUtil;

    // JpaAuditing
    @MockBean
    public JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockBean
    public JwtAuthFilter jwtAuthFilter;

    // Mock Data
    public User user;

    @BeforeEach
    public void setUp(WebApplicationContext wac, RestDocumentationContextProvider restDoc) throws ServletException, IOException {

        // Mock User
        user = User.builder()
                .email("test@gmail.com")
                .build();


        //로그인 정보 세팅
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(documentationConfiguration(restDoc))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

}
