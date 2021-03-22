package kennan.co.ke.peergame.view.auth

import com.vaadin.flow.component.orderedlayout.VerticalLayout

import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.login.LoginForm

import com.vaadin.flow.router.BeforeEnterEvent

import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import kennan.co.ke.peergame.security.CustomRequestCache
import org.springframework.security.authentication.AuthenticationManager
import java.util.*
import com.vaadin.flow.component.UI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException


@Route(value = LoginView.ROUTE)
@PageTitle("Login")
@HtmlImport("frontend://bower_components/iron-form/iron-form.html")


class LoginView(
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val requestCache: CustomRequestCache
) : VerticalLayout(), BeforeEnterObserver {

    companion object {
        const val ROUTE = "login"
    }

    private val loginForm: LoginForm = LoginForm()

    init {
        loginForm.action = "login"
        loginForm.isError = false
        element.appendChild(loginForm.element)

        loginForm.addLoginListener { e ->
            try {
                // try to authenticate with given credentials, should always return not null or throw an {@link AuthenticationException}
                val authentication: Authentication = authenticationManager
                    .authenticate(UsernamePasswordAuthenticationToken(e.username, e.password))


                // if authentication was successful we will update the security context and redirect to the page requested first
                SecurityContextHolder.getContext().authentication = authentication

                UI.getCurrent().navigate(requestCache.resolveRedirectUrl())
            } catch (ex: AuthenticationException) {

                // show default error message
                // Note: You should not expose any detailed information here like "username is known but password is wrong"
                // as it weakens security.
                loginForm.isError = true
            }
        }
    }


    override fun beforeEnter(event: BeforeEnterEvent?) {
        // inform the user about an authentication error
        if (event != null)
            if (event.location.queryParameters.parameters.getOrDefault("error", Collections.emptyList()).isEmpty())
                loginForm.isError = true
    }

}