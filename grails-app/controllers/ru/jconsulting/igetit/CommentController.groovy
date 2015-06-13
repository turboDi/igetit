package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException

@Secured(['ROLE_USER'])
class CommentController extends IGetItRestfulController<Comment> {

    CommentController() {
        super(Comment)
    }

    @Override
    protected List<Comment> listAllResources(Map params) {
        if (params.buyId) {
            def bid = params.buyId
            Comment.where {buy.id == bid}.list(params)
        } else {
            throw new IllegalStateException("Comments list requested without required 'buyId' parameter")
        }
    }

    @Override
    protected Comment createResource(Map params) {
        Comment comment = super.createResource(params) as Comment
        if (params.buyId) {
            comment.author = getAuthenticatedUser() as Person
            comment.buy = Buy.get(params.buyId as Serializable)
        }
        log.debug("User '$comment.author' is about to post comment: $comment.text")
        comment
    }

    @Override
    protected Comment queryForResource(Serializable id) {
        Comment comment = super.queryForResource(id) as Comment
        def currentUser = getAuthenticatedUser()
        if (request.method != 'GET' && comment && !comment.author.equals(currentUser)) {
            log.error("Invalid $request.method attempt by '$currentUser' of '$comment'")
            throw new AccessDeniedException('This comment belongs to another user')
        }
        comment
    }
}
