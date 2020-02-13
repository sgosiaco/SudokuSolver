import java.util.*

class Node() {

    private var column: Node
    private var up: Node
    private var down: Node
    private var left: Node
    private var right: Node
    var row: Int = -1
    var col: Int = -1
    var count = 0

    init {
        column = this
        up = this
        down = this
        left = this
        right = this
    }

    constructor(row: Int, col: Int) : this() {
        this.row = row
        this.col = col
    }

    fun generateLinks(board: Array<IntArray>) {
        //generate column heads
        for((colIndex, col) in board[0].withIndex()) {
            insertLeft(Node(-1, colIndex))
        }

        for((rowIndex, row) in board.withIndex()) {
            val newRow = Node()
            for((colIndex, col) in row.withIndex()) {
                if(col == 1) {
                    if(newRow.col == -1) {
                        newRow.col = colIndex
                        newRow.row = rowIndex
                    }
                    else {
                        newRow.insertLeft(Node(rowIndex, colIndex))
                    }
                }
            }
            insertRow(newRow)
        }
    }

    fun ALX(step: Int, solution: ArrayList<Node>) {
        if(right == this) {
            println("Soln")
            for(sl in solution)  {
                val row = sl.row/81
                val col = (sl.row/9)%9
                val num = (sl.row - (row * 81) - (col * 9)) + 1
                board[row][col] = num
            }
            for(row in board) {
                for(col in row) {
                    print("$col ")
                }
                print("\n")
            }
            println("")
            println("Took $step steps")
            return
        }

        val column = getMinCol() //choose column
        column.cover() //cover column

        var row = column.down
        while(row != column) {
            solution.add(row) //add row to solution
            row.coverRow() //cover row
            ALX(step + 1, solution) //try to continue
            //if we get back here, backtrack
            solution.remove(row) //remove row from soln
            row.uncoverRow() //uncover row
            row = row.down
        }
        column.uncover()
    }

    private fun coverSide() {
        left.right = right
        right.left = left
    }

    private fun uncoverSide() {
        left.right = this
        right.left = this
    }

    private fun coverTopBot() {
        up.down = down
        down.up = up
        column.count -= 1
    }

    private fun uncoverTopBot() {
        up.down = this
        down.up = this
        column.count += 1
    }

    private fun insertUp(node: Node) {
        up.down = node
        node.up = up
        up = node
        node.down = this
        column.count += 1
    }

    private fun insertLeft(node: Node) {
        left.right = node
        node.left = left
        left = node
        node.right = this
    }

    private fun insertRow(node: Node) {
        //only called by head head
        var row = node
        do {
            var column = right
            while(column.col != row.col) {
                column = column.right
            }
            column.insertUp(row)
            row.column = column
            row = row.right
        } while(row != node)
    }

    private fun cover() {
        with(column) {
            coverSide()
            var col = down
            while(col != this) {
                var row = col.right
                while(row != col) {
                    row.coverTopBot()
                    row = row.right
                }
                col = col.down
            }
        }
    }

    private fun uncover() {
        with(column) {
            uncoverSide()
            var col = down
            while(col != this) {
                var row = col.right
                while(row != col) {
                    row.uncoverTopBot()
                    row = row.right
                }
                col = col.down
            }
        }
    }

    private fun getMinCol() : Node {
        //only called  by head head
        var node = right
        var min = right
        while(node != this) {
            if(node.count < min.count)  {
                min = node
            }
            node = node.right
        }
        return min
    }

    private fun coverRow() {
        var node = right
        while(node != this) {
            node.cover()
            node = node.right
        }
    }

    private fun uncoverRow() {
        var node = right
        while(node != this) {
            node.uncover()
            node = node.right
        }
    }
}