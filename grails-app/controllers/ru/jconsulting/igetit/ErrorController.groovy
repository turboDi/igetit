package ru.jconsulting.igetit

import static org.springframework.http.HttpStatus.*

class ErrorController {

    def handleForbidden() {
        log.error("Forbidden url $request.forwardURI")
        render(status: FORBIDDEN, contentType: "application/json") {
            status = FORBIDDEN.value()
            message = 'You are not allowed to see this page'
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
