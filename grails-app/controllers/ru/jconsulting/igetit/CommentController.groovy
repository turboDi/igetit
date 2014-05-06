package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import ru.jconsulting.igetit.auth.User

@Secured(['ROLE_USER'])
class CommentController extends RestfulController<Comment> {

    static responseFormats = ['json']

    def springSecurityService

    CommentController() {
        super(Comment)
    }

    @Override
    protected List<Comment> listAllResources(Map params) {
        if (params.buyId) {
            def bid = params.buyId
            Comment.where {buy.id == bid}.list(params)
        } else {
            return super.listAllResources(params)
        }
    }

    @Override
    protected Comment createResource(Map params) {
        Comment comment = super.createResource(params) as Comment
        if (params.buyId) {
            comment.created = new Date()
            comment.author = springSecurityService.getCurrentUser() as User
            comment.buy = Buy.get(params.buyId as Serializable)
        }
        comment
    }
}
