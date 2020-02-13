import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

val board = arrayOf(
    intArrayOf(0, 2, 0, 0, 0, 0, 0, 0, 0),
    intArrayOf(0, 0, 0, 6, 0, 0, 0, 0, 3),
    intArrayOf(0, 7, 4, 0, 8, 0, 0, 0, 0),
    intArrayOf(0, 0, 0, 0, 0, 3, 0, 0, 2),
    intArrayOf(0, 8, 0, 0, 4, 0, 0, 1, 0),
    intArrayOf(6, 0, 0, 5, 0, 0, 0, 0, 0),
    intArrayOf(0, 0, 0, 0, 1, 0, 7, 8, 0),
    intArrayOf(5, 0, 0, 0, 0, 9, 0, 0, 0),
    intArrayOf(0, 0, 0, 0, 0, 0, 0, 4, 0)
)

fun main() {

    val head = Node()
    val solution =  ArrayList<Node>()


    for(row in board) {
        for(col in row) {
            print("$col ")
        }
        print("\n")
    }
    println("")
    var step = 0
    val time = measureTimeMillis {
        head.generateLinks(generateCover(board))
        head.ALX(step, solution)
    }
    println("Took $time millisec")
}

fun Int.pow(power: Int) : Int = this.toDouble().pow(power).toInt()

fun getRowIndex(row: Int, col: Int, num: Int, size: Int) = (row * size.pow(2)) + (col * size) + num

fun generateCover(board: Array<IntArray>) : Array<IntArray> {
    val size = board.size
    val square = size.pow(2)
    val colSize = square * 4
    val rowSize = size.pow(3)
    val boxSize = sqrt(size.toDouble()).toInt()
    val cover = Array(rowSize){ IntArray(colSize) {0} }

    var header = 0

    //constraints
    for(row in 0 until size) {
        for(col in 0 until size) {
            for(num in 0 until size) {
                cover[getRowIndex(row, col, num, size)][header] = 1 //row col (row, col, num)
                cover[getRowIndex(row, num, col, size)][header+square] = 1 //row (row, num, col)
                cover[getRowIndex(num, row, col, size)][header+square*2] = 1 //col (col, num, row)
            }
            header += 1
        }
    }

    header += square*2

    //box
    for(box in 0 until size) {
        for(spot in 0 until size) {
            val row = spot/boxSize + ((box/boxSize) * boxSize)
            val col = spot%boxSize + ((box%boxSize) * boxSize)
            for(num in 0 until size) {
                cover[getRowIndex(row, col, num, size)][header + num + box*size] = 1
            }
        }
    }

    for(row in 0 until size) {
        for(col in 0 until size) {
            if(board[row][col] != 0) {
                for(num in 1..9) {
                    if(board[row][col] != num) {
                        for(header in 0 until colSize) {
                            cover[getRowIndex(row, col, num-1, size)][header] = 0
                        }
                    }
                }
            }
        }
    }

    return cover
}

