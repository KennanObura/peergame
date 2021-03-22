package kennan.co.ke.peergame.core.service.user

import kennan.co.ke.peergame.core.model.UserModel
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
interface UserService {

    fun getUsers(): List<UserModel>

    fun registerUser(user: UserModel): ResponseEntity<UserModel>

    fun editUser(id: Long, user: UserModel): ResponseEntity<UserModel>

    fun removeUser(id: Long): ResponseEntity<Void>

    fun getUserById(id: Long): ResponseEntity<UserModel>

    fun findByLastNameStartsWithIgnoreCase(lastName: String?): List<UserModel>

    fun findByEmail(email : String) : UserModel

    fun emailAlreadyExist(user: UserModel) : Boolean

}