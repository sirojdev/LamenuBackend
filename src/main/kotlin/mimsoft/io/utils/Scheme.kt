package mimsoft.io.utils

import mimsoft.io.repository.DBManager

object Scheme {


    fun runAll(){
        editUser()
    }
    fun editUser(){
        val query = "alter table users add column test_column text"

    }


}