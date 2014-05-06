package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Comment

class CommentMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Comment) { Comment comment ->
            return [
                    id: comment.id,
                    created: comment.created,
                    text: comment.text,
                    authorName: comment.author.username,
                    authorId: comment.author.id,
                    rating: (comment.getAverageRating())
            ]
        }
    }
}
