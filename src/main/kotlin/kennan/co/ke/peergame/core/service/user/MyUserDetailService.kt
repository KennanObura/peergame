package kennan.co.ke.peergame.core.service.user

import kennan.co.ke.peergame.core.model.Role
import kennan.co.ke.peergame.core.model.UserModel
import kennan.co.ke.peergame.core.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class MyUserDetailService(private val repository: UserRepository) : UserDetailsService {


    override fun loadUserByUsername(email: String): UserDetails {
        val currentUser: UserModel = repository.findByEmail(email) ?: throw UsernameNotFoundException("Invalid email")


        return User.withUsername(currentUser.email)
            .password(currentUser.password)
            .roles(Role.REGULAR.name)
            .build()
    }
}

/*





 boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        return  new org.springframework.security.core.userdetails.User
          (user.getEmail(),
          user.getPassword().toLowerCase(), enabled, accountNonExpired,
          credentialsNonExpired, accountNonLocked,
          getAuthorities(user.getRoles()));



 */