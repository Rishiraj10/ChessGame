import korlibs.datastructure.*

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
    override fun toString(): String {
       return (0 until array.height).joinToString("\n") { y ->
            (0 until array.width).joinToString("") { x ->
                array[x, y].toString()
            }

        }

    }
}
