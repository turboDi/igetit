class UrlMappings {

	static mappings = {
        "/persons"(resources: "person") {
            "/buys"(resources: "buy") {
                "/comments"(resources: "comment") {
                    "/likes"(resources: "like", includes: ['index', 'save', 'delete']) {
                        likeableId = { params.commentId }
                        type = 'comment'
                    }
                }
                "/likes"(resources: "like", includes: ['index', 'save', 'delete']) {
                    likeableId = { params.buyId }
                    type = 'buy'
                }
                "/images"(resources: "image")
            }
            "/followers"(resources: "personFollower", includes: ['index', 'save', 'delete'])
            "/followed"(controller: "personFollowed")
        }
        "/buys"(resources: "buy") {
            "/comments"(resources: "comment") {
                "/likes"(resources: "like", includes: ['index', 'save', 'delete']) {
                    likeableId = { params.commentId }
                    type = 'comment'
                }
            }
            "/likes"(resources: "like", includes: ['index', 'save', 'delete']) {
                likeableId = { params.buyId }
                type = 'buy'
            }
            "/images"(resources: "image")
        }
        "/favorites"(resources: "personFavorite", includes: ['index', 'save', 'delete'])
        "/comments"(resources: "comment") {
            "/likes"(resources: "like", includes: ['index', 'save', 'delete']) {
                likeableId = { params.commentId }
                type = 'comment'
            }
        }
        "/brands"(resources: "brand")
        "/categories"(resources: "category")

        "/storage/$folderId?"(controller: "storage") {
            action = [POST: "upload", DELETE: "delete"]
        }

        "/account/$action/$oAuthProvider?"(controller: "account")
        "/subscription/$action"(controller: "subscription")
        "/search/$action"(controller: "search")

        "403"(controller: "error", action: "handleForbidden")
        "404"(controller: "error", action: "handleNotFound")
        "500"(controller: "error", action: "handleError")
	}
}
