package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Comment

class CommentMarshaller extends BaseMarshaller implements MarshallerRegistrar {

    @Override
    void register() {
        JSON.registerObjectMarshaller(Comment) { Comment comment ->
            return [
                    id: comment.id,
                    dateCreated: comment.dateCreated,
                    text: comment.text,
                    author: marshallPerson(comment.author),
                    likes: comment.getTotalLikes(),
                    iLiked: comment.userLiked(currentPerson())
            ]
        }
    }
}
