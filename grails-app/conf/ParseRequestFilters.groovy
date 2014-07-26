class ParseRequestFilters {

    def filters = {

        saveOrUpdateAction(action: 'save|update') {
            before = {
                if (request.JSON) {
                    params << request.JSON
                }
            }
        }

    }
}
