package mimsoft.io.utils

object Scheme {

  fun runAll() {
    editUser()
  }

  fun editUser() {
    val query = "alter table users add column test_column text"
  }
}
