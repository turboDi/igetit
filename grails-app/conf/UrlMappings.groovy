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
	}
}
