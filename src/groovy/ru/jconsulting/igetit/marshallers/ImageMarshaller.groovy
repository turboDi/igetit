package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Image

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 05.05.14 21:46
 */
class ImageMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Image){ Image image ->
            return [
                    filename: image.filename,
                    path: image.path
            ]
        }
    }
}
