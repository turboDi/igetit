package ru.jconsulting.igetit

import grails.rest.RestfulController

class CommentController extends RestfulController<Comment> {

    static responseFormats = ['json']

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
}
