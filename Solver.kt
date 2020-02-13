import kotlin.system.measureTimeMillis

data class empty(val row: Int, val col: Int, var guess: Int)

fun main() {
    val boardEasy = arrayOf(
        intArrayOf(0, 0, 0, 2, 6, 0, 7, 0, 1),
        intArrayOf(6, 8, 0, 0, 7, 0, 0, 9, 0),
        intArrayOf(1, 9, 0, 0, 0, 4, 5, 0, 0),
        intArrayOf(8, 2, 0, 1, 0, 0, 0, 4, 0),
        intArrayOf(0, 0, 4, 6, 0, 2, 9, 0, 0),
        intArrayOf(0, 5, 0, 0, 0, 3, 0, 2, 8),
        intArrayOf(0, 0, 9, 3, 0, 0, 0, 7, 4),
        intArrayOf(0, 4, 0, 0, 5, 0, 0, 3, 6),
        intArrayOf(7, 0, 3, 0, 1, 8, 0, 0, 0)
    )

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

    val zeros = ArrayList<empty>()

    for (row in 0..8) {
        for (col in 0..8) {
            if (board[row][col] == 0) {
                zeros.add(empty(row, col, 0))
            }
        }
    }

    var cur = 0
    var steps = 0

    val time = measureTimeMillis {
        while (cur < zeros.size) {
            with(zeros[cur]) {
                do {
                    guess += 1
                } while (checkAll(guess, row, col, board) && guess < 10)
                if (guess == 10) {
                    guess = 0
                    board[zeros[cur - 1].row][zeros[cur - 1].col] = 0
                    cur -= 2
                } else {
                    board[row][col] = guess
                }
            }
            cur += 1
            steps += 1
        }
    }

    println("Checking board")
    println(checkBoard(board))

    for (row in board) {
        for (col in row) {
            print("$col ")
        }
        print("\n")
    }
    println("Took $steps steps. Took $time millisec")
}


//returns true if board is correct, otw. false
fun checkBoard(board: Array<IntArray>): Boolean {
    val colArray = Array(9) { IntArray(9) {0} }
    val boxArray = Array(3) { Array(3) {IntArray(9)} }
    val three: Int = 3

    //check rows
    for ((rowIndex, row) in board.withIndex()) {
        if (row.distinct().size != 9) {
            return false
        }
        for ((colIndex, num) in row.withIndex()) {
            colArray[colIndex][rowIndex] = num
            boxArray[rowIndex / three][colIndex / three][(colIndex % 3) + ((rowIndex % 3) * 3)] = num
        }
    }

    //check columns
    for (col in colArray) {
        if (col.distinct().size != 9) {
            return false
        }
    }

    //check boxes
    for (row in boxArray) {
        for (box in row) {
            if (box.distinct().size != 9) {
                return false
            }
        }
    }
    return true
}

//returns true if test is in row, col, box, otw. false
fun checkAll(test: Int, row: Int, col: Int, board: Array<IntArray>): Boolean {
    return checkRow(test, row, board) || checkCol(test, col, board) || checkBox(test, ((row / 3) * 3) + (col / 3), board)
}

//returns true if test is in row, otw. false
fun checkRow(test: Int, row: Int, board: Array<IntArray>): Boolean {
    if (test in board[row]) {
        return true
    }
    return false
}

//returns true if test is in col, otw. false
fun checkCol(test: Int, col: Int, board: Array<IntArray>): Boolean {
    for (row in board) {
        if (test == row[col]) {
            return true
        }
    }
    return false
}

//returns true if test is in box, otw. false
fun checkBox(test: Int, box: Int, board: Array<IntArray>): Boolean {
    for (rowIndex in ((box / 3) * 3)..(((box / 3) * 3) + 2)) {
        for (x in (box % 3) * 3..(((box % 3) * 3) + 2)) {
            if (test == board[rowIndex][x]) {
                return true
            }
        }
    }
    return false
}