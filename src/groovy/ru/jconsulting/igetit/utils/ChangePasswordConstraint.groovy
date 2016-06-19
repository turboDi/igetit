package ru.jconsulting.igetit.utils

import org.codehaus.groovy.grails.validation.AbstractConstraint
import org.springframework.validation.Errors
import ru.jconsulting.igetit.Person

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 4/19/2016
 */
class ChangePasswordConstraint extends AbstractConstraint {

    public static final String CHANGE_PASSWORD_CONSTRAINT = "changePassword"

    private static enum Type {
        NEW, CURRENT
    }

    private Type type
    private passwordEncoder

    @Override
    boolean supports(Class type) {
        type == String && constraintOwningClass.isAssignableFrom(Person)
    }

    @Override
    String getName() {
        CHANGE_PASSWORD_CONSTRAINT
    }

    @Override
    void setParameter(Object constraintParameter) {
        type = Type.valueOf(constraintParameter as String)
        super.setParameter(constraintParameter)
    }

    @Override
    protected void processValidate(Object target, Object propertyValue, Errors errors) {
        if (target.isAttached() && target.isDirty('password')) {
            String password = propertyValue
            String oldPassword = target.getPersistentValue('password')
            switch (type) {
                case Type.NEW:
                    if (passwordEncoder.isPasswordValid(oldPassword, password, null)) {
                        errors.rejectValue('password', 'validation.password.new.same.message', null, 'Please choose a different password from your current one')
                    }
                    break;
                case Type.CURRENT:
                    if (!password || !passwordEncoder.isPasswordValid(oldPassword, password, null)) {
                        errors.rejectValue('oldPassword', 'validation.password.old.invalid.message', null, 'Current password is incorrect')
                    }
                    break;
            }
        }
    }

    @Override
    protected boolean skipNullValues() {
        type == Type.NEW
    }
}
