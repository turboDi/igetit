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
            def buy = Buy.get(params.buyId)
            if (!buy) {
                throw new NotFoundException("There is no such buy with id=${params.buyId}")
            }
            Comment.findAllByBuyAndDeleted(buy, false, params)
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
            if (request.method == 'DELETE' && comment.buy.owner.equals(currentUser)) {
                return comment
            }
            log.error("Invalid $request.method attempt by '$currentUser' of '$comment'")
            throw new AccessDeniedException('This comment belongs to another user')
        }
        comment
    }
}
