import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.image.color.*
import korlibs.math.*
import korlibs.math.geom.*

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo { MyScene() }
}

class MyScene : PixelatedScene(128*8, 128*8, sceneSmoothing = true) {
	override suspend fun SContainer.sceneMain() {
        val images = __KR.KRGfx.chessShadow.read()
        val blacks = images.run{ listOf(
            b_pawn, b_bishop,
            b_knight, b_knight,
            b_rook, b_queen,
            b_king
        )
        }
        val whites = images.run{ listOf(
            w_pawn, w_bishop,
            w_knight, w_knight,
            w_rook, w_queen,
            w_king
        )
        }
        val bgs = images.run{ listOf(square_brown_dark, square_brown_light)}
        for (y in 0 until 8) {
            for (x in 0 until 8){
                image(bgs[(x+y).isOdd.toInt()]).xy(128*x, 128*y)
            }
        }
        for (n in 0 until 6){
            image(blacks[n]).centered.xy(n*128 + 64, 0+64).scale(0.9)
            image(whites[n]).centered.xy(n*128 + 64, 128+64).scale(0.9)


        }

    }
}
