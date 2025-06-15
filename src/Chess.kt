import ChessPieceKind.*
import korlibs.datastructure.*
import korlibs.math.geom.*

enum class ChessPieceKind(val key: Char, val index: Int) {

    PAWN('P',0),
    BISHOP('B', 1),
    KNIGHT('K',2),
    ROOK('R',3),
    QUEEN('Q',4),
    KING('N', 5)
}

enum class ChessPlayer {
    BLACK, WHITE
}

sealed interface ChessCell {
    val key: Char

    object Empty: ChessCell{
        override val key: Char get() = '.'
        override fun toString(): String = "$key"
    }
    data class Piece(
        val kind: ChessPieceKind,
        val player: ChessPlayer
    ): ChessCell {
        override val key: Char get() = when (player) {
            ChessPlayer.BLACK -> kind.key.uppercaseChar()
            ChessPlayer.WHITE -> kind.key.lowercaseChar()
        }
        override fun toString(): String = "$key"
    }
}



class ChessBoard(val array : Array2<ChessCell> = Array2<ChessCell>(8, 8) {ChessCell.Empty}) {
    companion object {
        fun createDefault(): ChessBoard {
            val board = ChessBoard()
            for (player in listOf(ChessPlayer.BLACK, ChessPlayer.WHITE)) {
              fun getY(y: Int): Int = when (player) {

                      ChessPlayer.BLACK -> y
                      ChessPlayer.WHITE ->  7 - y
                  }

                val pawn = ChessCell.Piece(PAWN, player)
                val rook = ChessCell.Piece(ROOK, player)
                val knight = ChessCell.Piece(KNIGHT, player)
                val bishop = ChessCell.Piece(BISHOP, player)
                val queen = ChessCell.Piece(QUEEN, player)
                val king = ChessCell.Piece(KING, player)
                for (n in 0 until 8) board[n, getY(1)] = pawn
                board[0, getY(0)] = rook
                board[1, getY(0)] = knight
                board[2, getY(0)] = bishop
                board[3, getY(0)] = queen
                board[4, getY(0)] = king
                board[5, getY(0)] = bishop
                board[6, getY(0)] = knight
                board[7, getY(0)] = rook

            }
            return board

        }
    }

    operator fun get(x: Int, y: Int): ChessCell = array[x, y]
    operator  fun set(x: Int, y: Int, value: ChessCell){ array[x, y] = value}

    operator fun get(p: PointInt): ChessCell = this[p.x, p.y]
    operator  fun set(p: PointInt, value: ChessCell){ this[p.x, p.y] = value}


    override fun toString(): String {
       return (0 until array.height).joinToString("\n") { y ->
            (0 until array.width).joinToString("") { x ->
                array[x, y].toString()
            }

        }

    }
    fun vailEmptyPosition(pos: PointInt): Boolean =
        pos.x in 0..7
            && pos.y in 0..7
            && this [pos] == ChessCell.Empty



        fun availableMovements(pos: PointInt): Set<PointInt> {
        val piece = this[pos]
        return when (piece){
            ChessCell.Empty -> return emptySet()
            is ChessCell.Piece -> {
                when(piece.kind){
                    ChessPieceKind.PAWN-> {
                        buildList {
                            val dir = when (piece.player) {
                                ChessPlayer.BLACK -> PointInt(0, +1)
                                ChessPlayer.WHITE -> PointInt(0, -1)

                            }
                            val isInitialPosition = when (piece.player) {
                               ChessPlayer.BLACK -> pos.y == 1
                               ChessPlayer.WHITE -> pos.y == 6
                           }
                            add(pos + dir)
                            if (isInitialPosition) {
                                add(pos + (dir.toFloat() * 2).toInt())
                            }
                        }
                    }
                    ChessPieceKind.BISHOP -> {
                        buildList {
                            for (dir in listOf(PointInt(-1,-1), PointInt(+1, -1), PointInt(-1,+1), PointInt(+1, +1))){
                                for (n in 1 .. 7){
                                    val newPos = pos + (dir.toFloat() * n.toFloat()).toInt()
                                    if(this@ChessBoard[newPos] != ChessCell.Empty) break
                                    add(newPos)
                                }
                            }
                        }
                    }
                    ChessPieceKind.KNIGHT -> {
                        listOf(
                            pos - PointInt(-1, -2),
                            pos - PointInt(+1, -2),
                            pos - PointInt(-1, +2),
                            pos - PointInt(+1, +2),

                            pos - PointInt(-2, -1),
                            pos - PointInt(+2, -1),
                            pos - PointInt(-2, +1),
                            pos - PointInt(+2, +1)
                        )
                    }
                    ChessPieceKind.ROOK -> TODO()
                    ChessPieceKind.QUEEN -> TODO()
                    ChessPieceKind.KING -> TODO()
                }
            }
        }.filter { vailEmptyPosition(it) }.toSet()
    }
}
