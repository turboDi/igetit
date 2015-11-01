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

        if (request.getHeader('Authorization')?.startsWith('Bearer')) {
            tokenValue = request.getHeader('Authorization').substring(7)
        }

        return tokenValue ? new AccessToken(tokenValue) : null
    }
}
