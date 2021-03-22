package kennan.co.ke.peergame.core.model

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull


@Entity
@Table(name = "users")
data class UserModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val displayName: String? = null,
    @NotNull
    val firstName: String? = null,
    val lastName: String? = null,
    val status: UserStatus = UserStatus.ACTIVE,
    val role: Role = Role.REGULAR,
    @Email
    val email: String? = null,
    val password: String? = null,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = LocalDateTime.now(),

    )


