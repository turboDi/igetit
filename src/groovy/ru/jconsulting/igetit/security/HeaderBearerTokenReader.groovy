package ru.jconsulting.igetit.security

import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.bearer.BearerTokenReader
import groovy.util.logging.Slf4j

import javax.servlet.http.HttpServletRequest

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 10/27/2015
 */
@Slf4j
class HeaderBearerTokenReader extends BearerTokenReader {

    @Override
    AccessToken findToken(HttpServletRequest request) {
        String tokenValue = null
        String header = request.getHeader('Authorization')

        if (header?.startsWith('Bearer')) {
            if (header.length() > 7) {
                tokenValue = header.substring(7)
            } else {
                log.error("Bearer Token Header had invalid format: {$header}")
            }
        }

        return tokenValue ? new AccessToken(tokenValue) : null
    }
}
