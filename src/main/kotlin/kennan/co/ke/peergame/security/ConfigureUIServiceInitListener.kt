package kennan.co.ke.peergame.security

import com.vaadin.flow.component.UI
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.server.ServiceInitEvent
import com.vaadin.flow.server.VaadinServiceInitListener
import kennan.co.ke.peergame.view.auth.LoginView
import org.springframework.stereotype.Component

@Component
class ConfigureUIServiceInitListener : VaadinServiceInitListener {
    override fun serviceInit(event: ServiceInitEvent?) {
        event?.source?.addUIInitListener { uiEvent ->
            run {

                val ui: UI = uiEvent.ui
                ui.addBeforeEnterListener(this::authenticateNavigation)
            }
        }
    }


    fun authenticateNavigation(event: BeforeEnterEvent) {
        if(!LoginView.equals(event.navigationTarget) && SecurityUtils.isUserLoggedIn.equals(true))
            event.rerouteTo(LoginView::class.java)

    }


}



//@Override
//public void serviceInit(ServiceInitEvent event) {
//    event.getSource().addUIInitListener(uiEvent -> { (2)
//        final UI ui = uiEvent.getUI();
//        ui.addBeforeEnterListener(this::authenticateNavigation);
//    });
//}
//
// private void authenticateNavigation(BeforeEnterEvent event) {
//    if (!LoginView.class.equals(event.getNavigationTarget())
//        && !SecurityUtils.isUserLoggedIn()) { (3)
//        event.rerouteTo(LoginView.class);
//    }
//}