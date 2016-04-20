package ru.jconsulting.igetit.utils

import org.codehaus.groovy.grails.validation.ConstrainedProperty
import org.codehaus.groovy.grails.validation.Constraint
import org.codehaus.groovy.grails.validation.ConstraintFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 4/19/2016
 */
class ConstraintRegistrar {

    @Autowired
    def passwordEncoder

    def register() {
        ConstrainedProperty.registerNewConstraint(ChangePasswordConstraint.CHANGE_PASSWORD_CONSTRAINT, new ConstraintFactory() {
            @Override
            Constraint newInstance() {
                return new ChangePasswordConstraint(passwordEncoder: passwordEncoder)
            }
        })
    }
}
