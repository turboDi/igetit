import org.springframework.security.access.AccessDeniedException

class UrlMappings {

	static mappings = {
        "/persons"(resources: "person", version: "1.0", namespace: "v1") {
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
        "/favorites"(resources: "personFavorite", includes: ['index', 'save', 'delete'], version: "1.0", namespace: "v1")

        "/categories"(resources: "category", version: "1.0", namespace: "v1")
        "/cities"(resources: "city", version: "1.0", namespace: "v1") {
            "/shops"(resources: "shop", version: "1.0", namespace: "v1")
        }
        "/shops"(resources: "shop", version: "1.0", namespace: "v1")

        "/storage/$folderId?"(controller: "storage", version: "1.0", namespace: "v1") {
            action = [POST: "upload", DELETE: "delete"]
        }

        "/subscription/$action"(controller: "subscription", version: "1.0", namespace: "v1")
        "/search/$action"(controller: "search", version: "1.0", namespace: "v1")
        "/account/verification"(controller: "account", version: "1.0", namespace: "v1") {
            action = [POST: "sendVerification", GET: "verify"]
        }
        "/account/password"(controller: "account", version: "1.0", namespace: "v1") {
            action = [POST: "resetPassword"]
        }

        "403"(controller: "error", action: "handleForbidden")
        "404"(controller: "error", action: "handleNotFound")
        "500"(controller: "error", action: "handleAccessDeniedException", exception: AccessDeniedException)
        "500"(controller: "error", action: "handleError")
	}
}
