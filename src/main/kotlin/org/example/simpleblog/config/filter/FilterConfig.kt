package org.example.simpleblog.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    /**
     * 인증 처리
     *
     * 스프링 시큐리티
     *  - 필터나 인터셉터를 이용해 만든 강력한 인증처리 관련 프레임워크
     *
     *  앞으로 해볼 것
     *
     *  ==== backend ====
     *  1. 비동기 처리
     *  2. 파일 핸들링
     *  3. sse event + web socket을 활용한 실시간 챗봇
     *  4. aws 배포
     *  5. actuator + admin-server를 통한 간단한 모니터링
     *  6. code deploy + github action을 통한 CI/CD
     *  7. 스프링 시큐리티 + JWT 인증처리
     *  8. Junit + mock 테스트 환경설정
     *  9. restdoc을 통한 API 문서 자동화
     *  10. gradle 멀티 모듈을 통해서, domain을 공유하는 Batch 서버 작성
     *  11. 인메모리 concurrentHashmap을 통한 cache 적용
     *  12. 계층형 테이블 전략
     *  13. 스프링 클라우드 모듈들을 활용해 간단하게 MSA 환경 구축
     *  14. Docker 연동해 배포
     *
     * ==== frontend ====
     * 1. react-typescript 환경세팅
     * 2. recoil + zustand를 통한 상태관리
     * 3. pm2를 활용한 배포, 모니터링
     * 4. 정적 페이지 서버로서 s3에 배포
     * 5. next.js ? (미정) 를 활용해서 서버사이드렌더링 체험 + 검색엔진 최적화(SEO)
     * 6. antd를 활용한 ui 컴포넌트 활용
     * 7. 반응형 스타일링
     * 8. webpack 최적화 + usecallback을 활용한 렌더링 최적화
     *
     *
     */

//    @Bean
    fun registMyAuthFilter(): FilterRegistrationBean<MyAuthFilter> {

        val bean = FilterRegistrationBean(MyAuthFilter())
        bean.addUrlPatterns("/api/*")
        bean.order = 0

        return bean
    }

}