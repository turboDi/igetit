class BootStrap {

    def grailsApplication
    def eventProducer
    def personEmailListener
    def emailService
    def customMarshallerRegistrar

    def init = { servletContext ->
        customMarshallerRegistrar.register()
        grailsApplication.mainContext.addApplicationListener eventProducer
        personEmailListener.emailService = emailService
    }
}
