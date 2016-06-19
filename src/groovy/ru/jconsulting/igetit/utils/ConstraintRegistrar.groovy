package ru.jconsulting.igetit.utils

import org.codehaus.groovy.grails.validation.ConstrainedProperty
import org.codehaus.groovy.grails.validation.Constraint
import org.codehaus.groovy.grails.validation.ConstraintFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

import javax.annotation.PostConstruct

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 4/19/2016
 */
class ConstraintRegistrar {

    @Autowired
    @Qualifier("passwordEncoder")
    def passwordEncoder

    @PostConstruct
    def register() {
        ConstrainedProperty.registerNewConstraint(ChangePasswordConstraint.CHANGE_PASSWORD_CONSTRAINT, new ConstraintFactory() {
            @Override
            Constraint newInstance() {
                return new ChangePasswordConstraint(passwordEncoder: passwordEncoder)
            }
        })
    }
}
