package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.beans.factory.annotation.Autowired
import ru.jconsulting.igetit.Comment

class CommentMarshaller implements MarshallerRegistrar {

    @Autowired
    private SpringSecurityService springSecurityService

    @Override
    void register() {
        JSON.registerObjectMarshaller(Comment) { Comment comment ->
            return [
                    id: comment.id,
                    created: comment.created,
                    text: comment.text,
                    authorName: comment.author.username,
                    authorId: comment.author.id,
                    likes: comment.getTotalLikes(),
                    iLiked: comment.userLiked(springSecurityService.getCurrentUser())
            ]
        }
    }
}
