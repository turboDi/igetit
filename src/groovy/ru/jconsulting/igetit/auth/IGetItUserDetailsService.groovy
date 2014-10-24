package ru.jconsulting.igetit.auth

import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import ru.jconsulting.igetit.Person

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 11.10.14 17:04
 */
class IGetItUserDetailsService extends GormUserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
        Person.withTransaction { status ->
            def person = Person.findByUsernameOrEmail(username, username)
            if (!person) {
                log.warn "User not found: $username"
                throw new NoStackUsernameNotFoundException()
            }

            Collection<GrantedAuthority> authorities = loadAuthorities(person, username, loadRoles)
            createUserDetails person, authorities
        }
    }
}
