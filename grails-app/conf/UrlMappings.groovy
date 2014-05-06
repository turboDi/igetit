class UrlMappings {

	static mappings = {
        "/users"(resources: "user") {
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
