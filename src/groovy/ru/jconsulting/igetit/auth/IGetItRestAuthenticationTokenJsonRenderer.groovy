package ru.jconsulting.igetit.auth

import com.odobo.grails.plugin.springsecurity.rest.RestAuthenticationToken
import com.odobo.grails.plugin.springsecurity.rest.token.rendering.DefaultRestAuthenticationTokenJsonRenderer
import grails.converters.JSON

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 04.10.14 13:55
 */
class IGetItRestAuthenticationTokenJsonRenderer extends DefaultRestAuthenticationTokenJsonRenderer {

    @Override
    String generateJson(RestAuthenticationToken restAuthenticationToken) {
        def result = JSON.parse(super.generateJson(restAuthenticationToken))

        result["id"] = restAuthenticationToken.principal.id

        return result.toString()
    }
}
