package ru.jconsulting.igetit

import org.springframework.security.access.AccessDeniedException

import static org.springframework.http.HttpStatus.*

class ErrorController {

    def handleForbidden() {
        if (request.forwardURI != '/') {
            log.error("Forbidden action $request.method $request.forwardURI")
            render(status: FORBIDDEN, contentType: "application/json") {
                status = FORBIDDEN.value()
                message = 'You are not allowed to see this page'
            }
        }
    }

    def handleAccessDeniedException(AccessDeniedException e) {
        render(status: FORBIDDEN, contentType: "application/json") {
            status = FORBIDDEN.value()
            message = e.message
        }
    }

    def handleNotFound() {
        log.error("Wrong url $request.forwardURI")
        render(status: NOT_FOUND, contentType: "application/json") {
            status = NOT_FOUND.value()
            message = 'There is no such page'
        }
    }

    def handleError() {
        Throwable exception = request.exception
        render(status: INTERNAL_SERVER_ERROR, contentType: "application/json") {
            status = INTERNAL_SERVER_ERROR.value()
            message = exception.message
            exceptionClass = exception.cause?.class
        }
    }
}
