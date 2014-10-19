class UrlMappings {

	static mappings = {
        "/storage/$action"(controller: "storage")

        "/likeable/$action"(controller: "likeable")

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

        "403"(controller: "error", action: "handleForbidden")
        "404"(controller: "error", action: "handleNotFound")
        "500"(controller: "error", action: "handleError")
	}
}
