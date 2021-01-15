package spring_security.spring_security.controller

import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class OAuthController : WebSecurityConfigurerAdapter() {

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal principal: OAuth2User): Map<String, Any> {
        return Collections.singletonMap("name", principal.getAttribute("name"))
    }

    override fun configure(http: HttpSecurity?) {
//        super.configure(http)
        if (http != null) {
            /**
             * ログイン処理
             */
            http.authorizeRequests()
                    .antMatchers("/", "/error", "/webjars/**").permitAll()
                    .anyRequest().authenticated()
            http.exceptionHandling().authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            http.oauth2Login()

            /**
             * ログアウト処理
             */
            http.logout().logoutSuccessUrl("/").permitAll()

            http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        }

    }


//    override fun configure(http: HttpSecurity) {
//        http.authorizeRequests(
//                a -> a.antMatchers("/", "/error", "webjars/**").permitAll().anyRequest().authenticated()
//        )
//        .exceptionHandling(e->e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
//    }

}
