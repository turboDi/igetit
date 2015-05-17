import org.springframework.security.access.AccessDeniedException

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
                "/images"(resources: "image", includes: ['index', 'save', 'delete'])
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
            "/images"(resources: "image", includes: ['index', 'save', 'delete'])
        }
        "/favorites"(resources: "personFavorite", includes: ['index', 'save', 'delete'])

        "/categories"(resources: "category")

        "/storage/$folderId?"(controller: "storage")

        "/account/$action/$oAuthProvider?"(controller: "account")
        "/subscription/$action"(controller: "subscription")
        "/search/$action"(controller: "search")

        "403"(controller: "error", action: "handleForbidden")
        "404"(controller: "error", action: "handleNotFound")
        "500"(controller: "error", action: "handleAccessDeniedException", exception: AccessDeniedException)
        "500"(controller: "error", action: "handleError")
	}
}
