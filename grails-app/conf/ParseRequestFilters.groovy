class ParseRequestFilters {

    def filters = {

        saveAction(action: 'save|update') {
            before = {
                if (request.JSON) {
                    params << request.JSON
                }
            }
        }

    }
}
