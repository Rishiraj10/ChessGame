import korlibs.datastructure.*
import korlibs.image.bitmap.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.view.filter.*
import korlibs.math.*
import korlibs.math.geom.*
import korlibs.math.geom.slice.*

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo { MyScene() }
}

class MyScene : PixelatedScene(128*8, 128*8, sceneSmoothing = true) {
	override suspend fun SContainer.sceneMain() {
        val container = container {}
        val placeholderContainer = container { }
        val text = text("position", textSize = 64f, color = Colors.RED)
            .filters(DropshadowFilter(dropX = 1f, dropY = 1f, blurRadius = 1f))
        val images = __KR.KRGfx.chessShadow.read()
        val blacks = images.run{ listOf(
            b_pawn, b_bishop,
            b_knight, b_rook,
            b_queen,
            b_king
        )
        }
        val whites = images.run{ listOf(
            w_pawn, w_bishop,
            w_knight, w_rook,
            w_queen,
            w_king
        )
        }
        val bgs = images.run{ listOf(square_brown_dark, square_brown_light)}

        val board = ChessBoard.createDefault()
        board[4, 4] = ChessCell.Piece(ChessPieceKind.KNIGHT, ChessPlayer.WHITE)
        board[3, 4] = ChessCell.Piece(ChessPieceKind.BISHOP, ChessPlayer.WHITE)
        board[2, 4] = ChessCell.Piece(ChessPieceKind.PAWN, ChessPlayer.WHITE)

        for (y in 0 until 8) {
            for (x in 0 until 8){
                container.image(bgs[(x+y).isEven.toInt()]).xy(128*x, 128*y).apply {
                    mouse{
                        this.onMove {
                            val movements = try {
                                board.availableMovements(PointInt(x, y))
                            } catch (e: Throwable){
                                emptyList()
                            }
                            text.text = "$x, $y : $movements"
                            placeholderContainer.removeChildren()
                            for (movement in movements){
                                placeholderContainer.solidRect(128, 128 , Colors.GREENYELLOW.withAd(0.25))
                                    .xy(movement.x * 128f, movement.y * 128f)
                            }

                        }

                    }
                }
            }
        }



        container.representBoard(board,blacks, whites)
        //for (n in 0 until 6){
         //   image(blacks[n]).centered.xy(n*128 + 64, 0+64).scale(0.9)
         //   image(whites[n]).xy(n*128 + 64, 128)
      //  }

    }

    private fun Container.representBoard(
        board: ChessBoard = ChessBoard(),
        blacks: List<RectSlice<out Bitmap>>,
        whites: List<RectSlice<out Bitmap>>
    ) {


        fun ChessCell.getImage(): BmpSlice {
            return when (this) {
                ChessCell.Empty -> Bitmaps.transparent
                is ChessCell.Piece -> {
                    val array = when (this.player) {
                        ChessPlayer.BLACK -> blacks
                        ChessPlayer.WHITE -> whites
                    }
                    array[this.kind.index]
                }
            }
        }

        board.array.each { x, y, v ->
            image(v.getImage()).centered.xy(x * 128 + 64, y * 128 + 64).scale(0.9)
        }
    }
}
