package ru.jconsulting.igetit

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
        log.debug "Looking for bearer token in Authorization header, query string or Form-Encoded body parameter"
        String tokenValue = null

        if (request.getHeader('Authorization')?.startsWith('Bearer')) {
            log.debug "Found bearer token in Authorization header"
            tokenValue = request.getHeader('Authorization').substring(7)
        } else {
            log.debug "No token found"
        }
        return tokenValue ? new AccessToken(tokenValue) : null
    }
}
