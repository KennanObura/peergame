package kennan.co.ke.peergame

import kennan.co.ke.peergame.core.model.UserModel
import kennan.co.ke.peergame.core.repository.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class PeergameApplication {

    @Bean

    fun run(repository: UserRepository) = ApplicationRunner {
        repository.save(
            UserModel(
                displayName = "Anonymous",
                firstName = "User",
                lastName = "Anonymous",
                email = "anonymous@gmail.com",
                password = "kenya1234"
            )
        )

    }
}

fun main(args: Array<String>) {
    runApplication<PeergameApplication>(*args)
}
