package kennan.co.ke.peergame.security

import com.vaadin.flow.server.HandlerHelper
import com.vaadin.flow.shared.ApplicationConstants
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

import javax.servlet.http.HttpServletRequest

class SecurityUtils {

    companion object {

        val isUserLoggedIn: () -> Boolean = {
            val auth: Authentication = SecurityContextHolder.getContext().authentication
            (auth !is AnonymousAuthenticationToken && auth.isAuthenticated)
        }


        val isFrameworkInternalRequest: (request: HttpServletRequest) -> Boolean = {
            val paramValue: String? = it.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER)
            (HandlerHelper.RequestType.values().any { r -> r.identifier.equals(paramValue) })
        }
    }

}


