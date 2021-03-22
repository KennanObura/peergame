package kennan.co.ke.peergame.core.repository

import kennan.co.ke.peergame.core.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional


@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface UserRepository : JpaRepository<UserModel, Long> {

    fun findByEmail(email: String) : UserModel

}