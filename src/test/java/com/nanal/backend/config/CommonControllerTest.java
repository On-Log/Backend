package com.nanal.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.interceptor.AuthInterceptor;
import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.security.User;
import com.nanal.backend.global.security.jwt.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class CommonControllerTest {
    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    public TokenUtil tokenUtil;

    @MockBean
    public AuthInterceptor authInterceptor;

    @MockBean
    public MemberRepository memberRepository;


    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) throws Exception {

        Member member = Member.builder()
                .memberId(1L)
                .email("test@test.com")
                .name("tester")
                .role(Member.Role.USER)
                .socialId("KAKAO@123")
                .nickname("logger")
                .build();

        // user.getSocialId() ?????? NullPointerException ????????? ?????? Authentication ??????
        AuthenticationUtil.makeAuthentication(member.getSocialId(), member.getEmail());

        given(tokenUtil.verifyToken(any())).willReturn(true);
        given(tokenUtil.getUid(any())).willReturn(member.getSocialId());
        given(memberRepository.findBySocialId(any())).willReturn(Optional.of(member));

        //???????????? ??????
        given(authInterceptor.preHandle(any(), any(), any())).willReturn(true);


        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))  // rest docs ?????? ??????
                .alwaysDo(MockMvcResultHandlers.print()) // andDo(print()) ?????? ??????
                .alwaysDo(restDocs) // pretty ????????? ?????? ???????????? ??? ???????????? ??????
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // ?????? ?????? ??????
                .build();
    }
}

