package kennan.co.ke.peergame.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasAuthority
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.server.ServerHttpSecurity.http


@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter(){

}