package com.nanal.backend.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.interceptor.AuthInterceptor;
import com.nanal.backend.global.security.User;
import com.nanal.backend.global.security.jwt.JwtAuthFilter;
import com.nanal.backend.global.security.jwt.TokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class CommonControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public TokenUtil tokenUtil;

    @MockBean
    public AuthInterceptor authInterceptor;

    @MockBean
    public MemberRepository memberRepository;



    @BeforeEach
    public void setUp(WebApplicationContext wac, RestDocumentationContextProvider restDoc) throws Exception {

        Member member = Member.builder()
                .memberId(1L)
                .email("test@test.com")
                .name("tester")
                .role(Member.Role.USER)
                .socialId("KAKAO@123")
                .nickname("logger")
                .build();

        given(tokenUtil.verifyToken(any())).willReturn(true);
        given(tokenUtil.getUid(any())).willReturn(member.getSocialId());
        given(memberRepository.findBySocialId(any())).willReturn(Optional.of(member));

        //인터셉터 통과
        given(authInterceptor.preHandle(any(), any(), any())).willReturn(true);



       /* // Mock User
        User user = User.builder()
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
*/

    }

}
