class UrlMappings {

	static mappings = {
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
