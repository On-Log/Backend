package com.nanal.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanal.backend.domain.auth.entity.Member;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import com.nanal.backend.global.interceptor.AuthInterceptor;
import com.nanal.backend.global.security.AuthenticationUtil;
import com.nanal.backend.global.security.jwt.TokenUtil;
import com.nanal.backend.global.throttling.ThrottlingInterceptor;
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
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;

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

//    @MockBean
//    public AuthInterceptor authInterceptor;

    @MockBean
    public ThrottlingInterceptor throttlingInterceptor;

    @MockBean
    public MemberRepository memberRepository;

    @MockBean
    public ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    public AccessDeniedHandler accessDeniedHandler;

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

        // user.getSocialId() 에서 NullPointerException 방지를 위한 Authentication 생성
        AuthenticationUtil.makeAuthentication(member);

        //given(tokenUtil.verifyToken(any())).willReturn(true);
        //given(tokenUtil.getSocialId(any())).willReturn(member.getSocialId());
        //given(tokenUtil.getEmail(any())).willReturn(member.getEmail());

        //인터셉터 통과
        given(throttlingInterceptor.preHandle(any(), any(), any())).willReturn(true);


        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))  // rest docs 설정 주입
                .alwaysDo(MockMvcResultHandlers.print()) // andDo(print()) 코드 포함
                .alwaysDo(restDocs) // pretty 패턴과 문서 디렉토리 명 정해준것 적용
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // 한글 깨짐 방지
                .build();
    }
}

