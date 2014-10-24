package ru.jconsulting.igetit

import static org.springframework.http.HttpStatus.*

class ErrorController {

    def handleForbidden() {
        if (!isKaffeine() || request.forwardURI != '/') {
            log.error("Forbidden action $request.method $request.forwardURI")
            render(status: FORBIDDEN, contentType: "application/json") {
                status = FORBIDDEN.value()
                message = 'You are not allowed to see this page'
            }
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

    private boolean isKaffeine() {
        def ipAddress = request.getHeader("Client-IP") ?:
                request.getHeader("X-Forwarded-For") ?:
                        request.remoteAddr
        ipAddress == '54.197.11.214'
    }
}
