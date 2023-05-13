package mimsoft.io.config

import mimsoft.io.utils.DBManager

fun configureDatabase() {
    DBManager.init()
}