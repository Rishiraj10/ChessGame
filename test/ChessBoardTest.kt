import korlibs.math.geom.*
import kotlin.test.*


class ChessBoardTest {
    @Test
    fun testEmptyBoard() {
        val board = ChessBoard()
        assertEquals(
            """
               ........
               ........
               ........
               ........
               ........
               ........
               ........
               ........
            """.trimIndent(),
            board.toString()
        )
    }


    @Test
    fun testDefaultBoard() {
        assertEquals(
            """
                RKBQNBKR
                PPPPPPPP
                ........
                ........
                ........
                ........
                pppppppp
                rkbqnbkr
            """.trimIndent(),
            ChessBoard.createDefault().toString()
        )
    }


    @Test
    fun testKnightMovements() {
        val board = ChessBoard.createDefault()

        assertEquals(
          setOf(PointInt(0, 2), PointInt(2, 2)),
            board.availableMovements(PointInt(1, 0))
        )

    }


    @Test
    fun testSimpleBoard() {
        val board = ChessBoard()
        board.array[0, 0] = ChessCell.Piece(ChessPieceKind.BISHOP, ChessPlayer.BLACK)
        board.array[1, 0] = ChessCell.Piece(ChessPieceKind.KNIGHT, ChessPlayer.WHITE)
        board.array[7, 6] = ChessCell.Piece(ChessPieceKind.KING, ChessPlayer.BLACK)

        assertEquals(
            """
                Bk......
                ........
                ........
                ........
                ........
                ........
                .......N
                ........
            """.trimIndent(),
            board.toString()
        )

    }
}
