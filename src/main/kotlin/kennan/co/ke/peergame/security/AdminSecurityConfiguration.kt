package kennan.co.ke.peergame.security

import kennan.co.ke.peergame.core.model.Role
import kennan.co.ke.peergame.core.service.user.MyUserDetailService
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import java.lang.Exception
import org.springframework.security.config.annotation.web.builders.WebSecurity
import kennan.co.ke.peergame.view.auth.LoginView
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import javax.annotation.Resource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder


//@Configuration
//@EnableWebSecurity
//@Order(2)
//class AdminSecurityConfiguration : WebSecurityConfigurerAdapter() {
//
//    override fun configure(http: HttpSecurity) {
//        http
//            .authorizeRequests(
//                Customizer { authorize ->
//                    authorize
//                        .antMatchers("/admin/**").hasAuthority(Role.ADMIN.name)
//                        .antMatchers("/**").permitAll()
//                }
//            )
//            .httpBasic {}
//
//    }
//
//
//}


@Configuration
@EnableWebSecurity
@Order(1)
class BasicWebSecurityConfiguration(val userDetailService: MyUserDetailService) : WebSecurityConfigurerAdapter() {


    @Bean
    fun authProvider(): DaoAuthenticationProvider? {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authProvider())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.csrf().disable() // Register our CustomRequestCache that saves unauthorized access attempts, so
            // the user is redirected after login.

            .requestCache().requestCache(CustomRequestCache()) // Restrict access to our application.
            .and()
            .authorizeRequests() // Allow all flow internal requests.
            .antMatchers(REGISTER_URL)
            .permitAll()
            .requestMatchers(SecurityUtils.isFrameworkInternalRequest)
            .permitAll() // Allow all requests by logged in users.
            .anyRequest().authenticated() // Configure the login page.

            .and()
            .formLogin()
            .loginPage(LOGIN_URL)
            .permitAll() //

            .loginProcessingUrl(LOGIN_PROCESSING_URL)
            .failureUrl(LOGIN_FAILURE_URL)
            // Configure logout
            .and().logout()
            .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
    }


    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }


    @Bean
    fun requestCache(): CustomRequestCache {
        return CustomRequestCache()
    }

//    override fun userDetailsService(): UserDetailsService {
//        val normalUser: UserDetails = User.withUsername("user")
//            .password("{noop}password")
//            .roles(Role.REGULAR.name)
//            .build()
//
//
//        val adminUser: UserDetails = User.withUsername("user")
//            .password("{noop}password")
//            .roles(Role.ADMIN.name, Role.REGULAR.name)
//            .build()
//
//
//    }

    @Bean
    override fun userDetailsService(): UserDetailsService {
        return userDetailService
    }


    companion object {
        private const val LOGIN_PROCESSING_URL = "/login"
        private const val LOGIN_FAILURE_URL = "/login?error"
        private const val LOGIN_URL = "/login"
        private const val LOGOUT_SUCCESS_URL = "/login"
        private const val REGISTER_URL = "/register"
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers(
            "/VAADIN/**",
            "/favicon.ico",
            "/robots.txt",
            "/manifest.webmanifest",
            "/sw.js",
            "/offline.html",
            "/icons/**",
            "/images/**",
            "/styles/**",
            "/h2-console/**"
        )
    }
}

//
//@Configuration
//class WebConfiguration : WebSecurityConfigurerAdapter() {
//
//
//
//}