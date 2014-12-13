class UrlMappings {

	static mappings = {
        "/persons"(resources: "person") {
            "/buys"(resources: "buy") {
                "/comments"(resources: "comment")
                "/images"(resources: "image")
            }
        }
        "/buys"(resources: "buy") {
            "/comments"(resources: "comment")
            "/images"(resources: "image")
        }
        "/brands"(resources: "brand")
        "/categories"(resources: "category")

        "/storage/$folderId?"(controller: "storage") {
            action = [POST: "upload", DELETE: "delete"]
        }

        "/like"(controller: "likeable"){
            action = [POST: "like"]
        }

        "/account/$action"(controller: "account")
        "/subscription/$action"(controller: "subscription")

        "403"(controller: "error", action: "handleForbidden")
        "404"(controller: "error", action: "handleNotFound")
        "500"(controller: "error", action: "handleError")
	}
}
