package mimsoft.io.services.socket

enum class SocketType {
  // courier status
  CHAT,
  LOCATION,
  NOT_READ,
  ACCEPT,
  ORDER,
  BOOK,
  RESPONSE_CHAT,
  RESPONSE_ORDER,

  // waiter status
  NEW_ORDER,
  RESPONSE_ACCEPT,

  //  courier and waiter
  CONNECT
}
