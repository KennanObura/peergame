package kennan.co.ke.peergame.security

import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import javax.servlet.http.HttpServletResponse

import javax.servlet.http.HttpServletRequest
import kennan.co.ke.peergame.view.auth.LoginView

import org.springframework.security.web.savedrequest.DefaultSavedRequest

import com.vaadin.flow.server.VaadinServletResponse

import com.vaadin.flow.server.VaadinServletRequest
import org.springframework.security.web.savedrequest.SavedRequest


class CustomRequestCache : HttpSessionRequestCache() {
    override fun saveRequest(request: HttpServletRequest?, response: HttpServletResponse?) {
        if (!SecurityUtils.isFrameworkInternalRequest(request!!)) {
            super.saveRequest(request, response)
        }
    }

    fun resolveRedirectUrl(): String {
        val savedRequest: SavedRequest = getRequest(
            VaadinServletRequest.getCurrent().httpServletRequest,
            VaadinServletResponse.getCurrent().httpServletResponse
        )
        if (savedRequest is DefaultSavedRequest) {
            val requestURI = savedRequest.requestURI


            // check for valid URI and prevent redirecting to the login view
            if (requestURI != null && requestURI.isNotEmpty() && !requestURI.contains(LoginView.ROUTE)) { //
                return if (requestURI.startsWith("/")) requestURI.substring(1) else requestURI //
            }
        }

        // if everything fails, redirect to the main view
        return ""
    }
}