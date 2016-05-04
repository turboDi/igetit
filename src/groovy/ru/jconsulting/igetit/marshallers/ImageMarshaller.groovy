package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Image

import javax.annotation.Resource

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 05.05.14 21:46
 */
class ImageMarshaller implements MarshallerRegistrar {

    @Resource(name="storage")
    def storage

    @Override
    void register() {
        JSON.registerObjectMarshaller(Image){ Image image ->
            return [
                    filename: image.filename,
                    folderId: image.folderId,
                    url: storage.getURL(image)
            ]
        }
    }
}
