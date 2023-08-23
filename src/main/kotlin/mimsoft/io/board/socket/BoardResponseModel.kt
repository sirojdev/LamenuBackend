package mimsoft.io.board.socket

import mimsoft.io.utils.ResponseModel

data class BoardResponseModel(
    val inProgress: ResponseModel,
    val ready: ResponseModel
) {
}