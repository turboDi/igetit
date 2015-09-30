class ParseRequestFilters {

    def filters = {

        saveOrUpdateAction(action: 'save|update') {
            before = {
                if (request.JSON) {
                    params << request.JSON
                }
            }
        }

        indexAction(action: 'index|buys|persons') {
            before = {
                if (params.sort?.class?.isArray()) {
                    def sort = [:]
                    params.sort.eachWithIndex { s, i ->
                        sort[s] = params.order?.class?.isArray() ? getOrderByIndex(params.order, i) : params.order
                    }
                    params.sort = sort
                    params.remove('order')
                }
            }
        }

    }

    private static getOrderByIndex(orders, i) {
        orders.length > i ? orders[i] : getOrderByIndex(orders, i - 1)
    }
}
