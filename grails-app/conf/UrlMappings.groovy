import org.springframework.security.access.AccessDeniedException

class UrlMappings {

	static mappings = {
        "/api/persons"(resources: "person", version: "1.0", namespace: "v1") {
            "/buys"(resources: "buy", version: "1.0", namespace: "v1") {
                "/comments"(resources: "comment", version: "1.0", namespace: "v1") {
                    "/likes"(resources: "like", includes: ['index', 'save', 'delete']) {
                        likeableId = { params.commentId }
                        type = 'comment'
                    }
                }
                "/likes"(resources: "like", includes: ['index', 'save', 'delete']) {
                    likeableId = { params.buyId }
                    type = 'buy'
                }
                "/images"(resources: "image", includes: ['index', 'save', 'delete'], version: "1.0", namespace: "v1")
            }
            "/followers"(resources: "personFollower", includes: ['index', 'save', 'delete'], version: "1.0", namespace: "v1")
            "/followed"(controller: "personFollowed", version: "1.0", namespace: "v1")
        }
        "/api/buys"(resources: "buy", version: "1.0", namespace: "v1") {
            "/comments"(resources: "comment", version: "1.0", namespace: "v1") {
                "/likes"(resources: "like", includes: ['index', 'save', 'delete']) {
                    likeableId = { params.commentId }
                    type = 'comment'
                }
            }
            "/likes"(resources: "like", includes: ['index', 'save', 'delete']) {
                likeableId = { params.buyId }
                type = 'buy'
            }
            "/images"(resources: "image", includes: ['index', 'save', 'delete'], version: "1.0", namespace: "v1")
        }
        "/api/favorites"(resources: "personFavorite", includes: ['index', 'save', 'delete'], version: "1.0", namespace: "v1")

        "/api/categories"(resources: "category", version: "1.0", namespace: "v1")

        "/api/storage/$folderId?"(controller: "storage", version: "1.0", namespace: "v1")

        "/api/subscription/$action"(controller: "subscription", version: "1.0", namespace: "v1")
        "/api/search/$action"(controller: "search", version: "1.0", namespace: "v1")
        "/api/verification"(controller: "verification", version: "1.0", namespace: "v1")

        "403"(controller: "error", action: "handleForbidden")
        "404"(controller: "error", action: "handleNotFound")
        "500"(controller: "error", action: "handleAccessDeniedException", exception: AccessDeniedException)
        "500"(controller: "error", action: "handleError")
	}
}
