package ru.jconsulting.igetit.image

import org.imgscalr.Scalr

import java.awt.image.BufferedImage

import static org.imgscalr.Scalr.Method
import static org.imgscalr.Scalr.OP_ANTIALIAS

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 10.05.2015
 */
class Thumbnail {

    Integer size, x, y, width

    BufferedImage image

    def crop() {
        if (x != null && y != null && width && image) {
            def cropped = Scalr.crop(image, x, y, width, width)
            image.flush()
            image = cropped
        }
        this
    }

    def resize() {
        if (size && image) {
            def resized = Scalr.resize(image, Method.QUALITY, size, OP_ANTIALIAS)
            image.flush()
            image = resized
        }
        this
    }
}
