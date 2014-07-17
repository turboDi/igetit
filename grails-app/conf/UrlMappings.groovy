class UrlMappings {

	static mappings = {
        "/image/$action"(controller: "image")

        "/persons"(resources: "person") {
            "/buys"(resources: "buy") {
                "/comments"(resources: "comment")
            }
        }
        "/buys"(resources: "buy") {
            "/comments"(resources: "comment")
        }
        "/brands"(resources: "brand")
        "/categories"(resources: "category")

        "403"(controller: "error", action: "handleForbidden")
        "404"(controller: "error", action: "handleNotFound")
        "500"(controller: "error", action: "handleError")
	}
}
