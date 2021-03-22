package kennan.co.ke.peergame.core.service.user

import kennan.co.ke.peergame.core.model.Role
import kennan.co.ke.peergame.core.model.UserModel
import kennan.co.ke.peergame.core.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl(private val repository: UserRepository) : UserService {
    override fun getUsers(): List<UserModel> {
        return repository.findAll()
    }

    override fun registerUser(user: UserModel): ResponseEntity<UserModel> {

        //TODO Create custom exceptions : UserAlreadyExists
        if(this.emailAlreadyExist(user))
            throw Exception("User exists")

        return ResponseEntity.ok(repository.save(user))
    }

    override fun editUser(id: Long, user: UserModel): ResponseEntity<UserModel> {
        return repository.findById(id).map { currentUser ->
            val updatedUser: UserModel =
                currentUser
                    .copy(
                        displayName = user.displayName,
                        role = user.role,
                        status = user.status,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        email = user.email,
                        updatedAt = LocalDateTime.now()
                    )
            ResponseEntity.ok().body(repository.save(updatedUser))
        }.orElse(ResponseEntity.notFound().build())
    }

    override fun removeUser(id: Long): ResponseEntity<Void> {
        return repository.findById(id).map { user ->
            ResponseEntity.ok(repository.delete(user))
            ResponseEntity<Void>(HttpStatus.ACCEPTED)
        }.orElse(ResponseEntity.notFound().build())
    }

    override fun getUserById(id: Long): ResponseEntity<UserModel> {
        return repository.findById(id).map { user ->
            ResponseEntity.ok(user)
        }.orElse(ResponseEntity.notFound().build())
    }


    override fun findByLastNameStartsWithIgnoreCase(lastName: String?): List<UserModel> {
        TODO("Not yet implemented")
    }

    override fun findByEmail(email: String): UserModel {
        return repository.findByEmail(email)
    }

    override fun emailAlreadyExist(user: UserModel): Boolean {
        return this.findByEmail(user.email!!) != null
    }


}