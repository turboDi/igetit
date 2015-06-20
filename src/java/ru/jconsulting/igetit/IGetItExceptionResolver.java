package ru.jconsulting.igetit;

import org.codehaus.groovy.grails.web.errors.GrailsExceptionResolver;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Dmitriy Borisov
 * @created 6/19/2015
 */
public class IGetItExceptionResolver extends GrailsExceptionResolver {

    @Override
    protected void logStackTrace(Exception e, HttpServletRequest request) {
        if (e instanceof AccessDeniedException) {
            return;
        }
        super.logStackTrace(e, request);
    }
}
