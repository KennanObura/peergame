package kennan.co.ke.peergame.view.auth

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent
import com.vaadin.flow.component.HasValue.ValueChangeListener
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout


import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.data.binder.*

import com.vaadin.flow.data.validator.EmailValidator
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.ServiceException
import kennan.co.ke.peergame.core.model.UserModel
import kennan.co.ke.peergame.core.service.user.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired


@Route(RegisterView.ROUTE)
class RegisterView(
   @Autowired private val service: UserServiceImpl
) : VerticalLayout() {

    companion object {
        const val ROUTE = "register"
    }


    private  var allowMarketingBox: Checkbox
    private  var passwordField1: PasswordField
    private  var passwordField2: PasswordField
    private  var binder: Binder<UserModel>

    /**
     * Flag for disabling first run for password validation
     */
    private var enablePasswordValidation = false

    init {
        /*
         * Create the components we'll need
         */


        /*
         * Create the components we'll need
         */
        val title = H2("Signup form")

        val firstnameField = TextField("First name")
        val lastnameField = TextField("Last name")
        val handleField = TextField("User handle")
        val emailField = TextField("Email")
        emailField.isVisible = false

        //TODO create avatar class
        /*
        val avatarField = Avatar("Select Avatar image")
         */

        allowMarketingBox = Checkbox("Allow marketing")
        allowMarketingBox.style.set("padding-top", "10px")

        passwordField1 = PasswordField("Password required")
        passwordField2 = PasswordField("confirm password")

        val errorMessage: Span = Span()

        val submitButton: Button = Button("Register")
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)


        /*
       * Build the visible layout
       */

        // Create a FormLayout with all our components. The FormLayout doesn't have any
        // logic (validation, etc.), but it allows us to configure Responsiveness from


        val formLayout: FormLayout = FormLayout(
            title,
            firstnameField,
            lastnameField,
            handleField,
            //avatar,
            passwordField1,
            passwordField2,
            allowMarketingBox,
            emailField,
            errorMessage,
            submitButton
        )


        /*

         Restrict maximum width and center on page
         */
        formLayout.maxWidth = "500px"
        formLayout.style.set("margin", "0 auto")


        /*
         Allow the form layout to be responsive. On device widths 0-490px we have one
         column, then we have two. Field labels are always on top of the fields.

         */

        formLayout.setResponsiveSteps(
            FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
            FormLayout.ResponsiveStep("490px", 2, FormLayout.ResponsiveStep.LabelsPosition.TOP)
        )


        /*

        These components take full width regardless if we use one column or two (it
        just looks better that way)


         */
        formLayout.setColspan(title, 2)
//        formLayout.setColspan(avatarField, 2)
        formLayout.setColspan(errorMessage, 2)
        formLayout.setColspan(submitButton, 2)

        // Add some styles to the error message to make it pop out
        errorMessage.style.set("color", "var(--lumo-error-text-color)")
        errorMessage.style.set("padding", "15px 0")

        // Add the form to the page
        add(formLayout)


        /*
         * Set up form functionality
         */

        /*
         * Binder is a form utility class provided by Vaadin. Here, we use a specialized
         * version to gain access to automatic Bean Validation (JSR-303). We provide our
         * data class so that the Binder can read the validation definitions on that
         * class and create appropriate validators. The BeanValidationBinder can
         * automatically validate all JSR-303 definitions, meaning we can concentrate on
         * custom things such as the passwords in this class.
         */


        /*
         * Set up form functionality
         */

        /*
         * Binder is a form utility class provided by Vaadin. Here, we use a specialized
         * version to gain access to automatic Bean Validation (JSR-303). We provide our
         * data class so that the Binder can read the validation definitions on that
         * class and create appropriate validators. The BeanValidationBinder can
         * automatically validate all JSR-303 definitions, meaning we can concentrate on
         * custom things such as the passwords in this class.
         */
        binder = Binder(UserModel::class.java)

        // Basic name fields that are required to fill in

        // Basic name fields that are required to fill in
        binder.forField(firstnameField).asRequired().bind("firstname")
        binder.forField(lastnameField).asRequired().bind("lastname")

        // The handle has a custom validator, in addition to being required. Some values
        // are not allowed, such as 'admin'; this is checked in the validator.

        // The handle has a custom validator, in addition to being required. Some values
        // are not allowed, such as 'admin'; this is checked in the validator.
        binder.forField(handleField).withValidator(this::validateHandle).asRequired().bind("handle")

        // Here we use our custom Vaadin component to handle the image portion of our
        // data, since Vaadin can't do that for us. Because the AvatarField is of type
        // HasValue<AvatarImage>, the Binder can bind it automatically. The avatar is
        // not required and doesn't have a validator, but could.

        // Here we use our custom Vaadin component to handle the image portion of our
        // data, since Vaadin can't do that for us. Because the AvatarField is of type
        // HasValue<AvatarImage>, the Binder can bind it automatically. The avatar is
        // not required and doesn't have a validator, but could.


        //TODO

//        binder.forField(avatarField).bind("avatar")


        // Allow marketing is a simple checkbox

        // Allow marketing is a simple checkbox
        binder.forField(allowMarketingBox).bind("allowsMarketing")
        // EmailField uses a Validator that extends one of the built-in ones.
        // Note that we use 'asRequired(Validator)' instead of
        // 'withValidator(Validator)'; this method allows 'asRequired' to
        // be conditional instead of always on. We don't want to require the email if
        // the user declines marketing messages.
        // EmailField uses a Validator that extends one of the built-in ones.
        // Note that we use 'asRequired(Validator)' instead of
        // 'withValidator(Validator)'; this method allows 'asRequired' to
        // be conditional instead of always on. We don't want to require the email if
        // the user declines marketing messages.
        binder.forField(emailField).asRequired(VisibilityEmailValidator("Value is not a valid email address"))
            .bind("email")

        // Only ask for email address if the user wants marketing emails

        // Only ask for email address if the user wants marketing emails
        allowMarketingBox.addValueChangeListener { e: ComponentValueChangeEvent<Checkbox?, Boolean?>? ->

            // show or hide depending on the checkbox
            emailField.isVisible = allowMarketingBox.value

            // Additionally, remove the input if the user decides not to allow emails. This
            // way any input that ends up hidden on the page won't end up in the bean when
            // saved.
            if (!allowMarketingBox.value) {
                emailField.value = ""
            }
        }

        // Another custom validator, this time for passwords

        // Another custom validator, this time for passwords
        binder.forField(passwordField1).asRequired().withValidator(this::passwordValidator).bind("password")
        // We won't bind passwordField2 to the Binder, because it will have the same
        // value as the first field when correctly filled in. We just use it for
        // validation.

        // The second field is not connected to the Binder, but we want the binder to
        // re-check the password validator when the field value changes. The easiest way
        // is just to do that manually.
        // We won't bind passwordField2 to the Binder, because it will have the same
        // value as the first field when correctly filled in. We just use it for
        // validation.

        // The second field is not connected to the Binder, but we want the binder to
        // re-check the password validator when the field value changes. The easiest way
        // is just to do that manually.
        passwordField2.addValueChangeListener { e: ComponentValueChangeEvent<PasswordField?, String?>? ->

            // The user has modified the second field, now we can validate and show errors.
            // See passwordValidator() for how this flag is used.
            enablePasswordValidation = true
            binder.validate()
        }

        // A label where bean-level error messages go

        // A label where bean-level error messages go
        binder.setStatusLabel(errorMessage)

        // And finally the submit button

        // And finally the submit button
        submitButton.addClickListener { _ ->
            try {

                // Create empty bean to store the details into
                val detailsBean = UserModel()

                // Run validators and write the values to the bean
                binder.writeBean(detailsBean)

                // Call backend to store the data
                service.registerUser(detailsBean)

                // Show success message if everything went well
                showSuccess(detailsBean)
            } catch (e1: ValidationException) {
                // validation errors are already visible for each field,
                // and bean-level errors are shown in the status label.

                // We could show additional messages here if we want, do logging, etc.
            } catch (e2: ServiceException) {

                // For some reason, the save failed in the back end.

                // First, make sure we store the error in the server logs (preferably using a
                // logging framework)
                e2.printStackTrace()

                // Notify, and let the user try again.
                errorMessage.text = "Saving the data failed, please try again"
            }
        }
    }


    /*
     * Method to validate that:
     *
     *
     * 1) Password is at least 8 characters long
     *
     *
     * 2) Values in both fields match each other
     */
    private fun passwordValidator(pass1: String?, ctx: ValueContext): ValidationResult? {

        /*
         * Just a simple length check. A real version should check for password
         * complexity as well!
         */
        if (!(pass1 != null && pass1.length >= 8)) {
            return ValidationResult.error("Password should be at least 8 characters long")
        }
        if (!enablePasswordValidation) {
            // user hasn't visited the field yet, so don't validate just yet, but next time.
            enablePasswordValidation = true
            return ValidationResult.ok()
        }
        val pass2 = passwordField2.value
        return if (pass1 == pass2) {
            ValidationResult.ok()
        } else ValidationResult.error("Passwords do not match")
    }


    /*
     * We call this method when form submission has succeeded
     */
    private fun showSuccess(detailsBean: UserModel) {
        val notification: Notification = Notification.show("You are all set, welcome " + detailsBean.displayName)
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS)

        //TODO
        // Here you'd typically redirect the user to login view

    }


    /*
     * Method that demonstrates using an external validator. Here we ask the backend
     * if this handle is already in use.
     */
    private fun validateHandle(handle: String, ctx: ValueContext): ValidationResult? {
//        val errorMsg: String = service.validateHandle(handle)
//            ?:
        return ValidationResult.ok()
//        return ValidationResult.error(errorMsg)
    }


    /*
     * Custom validator class that extends the built-in email validator.
     *
     *
     * Ths validator checks if the field is visible before performing the
     * validation. This way, the validation is only performed when the user has told
     * us they want marketing emails.
     */
    inner class VisibilityEmailValidator(errorMessage: String?) : EmailValidator(errorMessage) {
        override fun apply(value: String, context: ValueContext): ValidationResult {
            return if (!allowMarketingBox.value) {
                // Component not visible, no validation
                ValidationResult.ok()
            } else {
                // normal email validation
                super.apply(value, context)
            }
        }
    }


}