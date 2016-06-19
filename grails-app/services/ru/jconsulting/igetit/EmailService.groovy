package ru.jconsulting.igetit

import org.springframework.context.i18n.LocaleContextHolder

class EmailService {

    def mailService
    def messageSource
    def grailsApplication

    def sendVerification(Person p) {
        try {
            def locale = LocaleContextHolder.getLocale()
            log.debug("Sending verification email to $p of locale $locale")
            mailService.sendMail {
                async true
                to p.email
                subject messageSource.getMessage('email.verify.subject.text', null, locale)
                html view: '/account/verify', model: [p: p, locale: locale, key: encodeKey(p), siteCfg: grailsApplication.config.site]
            }
        } catch (Exception e) {
            log.error('Exception during verification mail send', e)
        }
    }

    def sendPassword(Person p, newPassword) {
        try {
            def locale = LocaleContextHolder.getLocale()
            log.debug("Sending password email to $p of locale $locale")
            mailService.sendMail {
                async true
                to p.email
                subject messageSource.getMessage('email.password.subject.text', null, locale)
                html view: '/account/password', model: [p: p, newPassword: newPassword, locale: locale, siteCfg: grailsApplication.config.site]
            }
        } catch (Exception e) {
            log.error('Exception during password mail send', e)
        }
    }

    def verify(String key) {
        def params = decodeKey(key)
        Person p = Person.findNotDeletedByConfirmTokenAndEmail(params.confirmToken, params.email)
        if (p) {
            p.emailConfirmed = true
            p.save()
        } else {
            log.warn("Person not found for confirmToken=$params.confirmToken and email=$params.email")
        }
    }

    def encodeKey(attrs) {
        (attrs.email + ':' + attrs.confirmToken).bytes.encodeBase64().toString()
    }

    def decodeKey(String key) {
        assert key
        def tokens = new String(key.decodeBase64(), 'UTF-8').split(':')
        if (tokens?.size() == 2) {
            [email: tokens[0], confirmToken: tokens[1]]
        } else {
            log.warn("Malformed base64 confirmation key: $key")
            [:]
        }

    }
}
