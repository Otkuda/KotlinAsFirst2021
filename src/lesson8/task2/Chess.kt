@file:Suppress("UNUSED_PARAMETER")

package lesson8.task2

import java.lang.IllegalArgumentException
import kotlin.math.*
import lesson8.task3.Graph

/**
 * Клетка шахматной доски. Шахматная доска квадратная и имеет 8 х 8 клеток.
 * Поэтому, обе координаты клетки (горизонталь row, вертикаль column) могут находиться в пределах от 1 до 8.
 * Горизонтали нумеруются снизу вверх, вертикали слева направо.
 */
data class Square(val column: Int, val row: Int) {
    /**
     * Пример
     *
     * Возвращает true, если клетка находится в пределах доски
     */
    fun inside(): Boolean = column in 1..8 && row in 1..8

    /**
     * Простая (2 балла)
     *
     * Возвращает строковую нотацию для клетки.
     * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
     * Для клетки не в пределах доски вернуть пустую строку
     */
    fun notation(): String = if (!inside()) ""
    else "${('a' + column - 1)}$row"
}


/**
 * Простая (2 балла)
 *
 * Создаёт клетку по строковой нотации.
 * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
 * Если нотация некорректна, бросить IllegalArgumentException
 */
fun square(notation: String): Square {
    require(notation.length == 2)
    val column = if (notation.first() in 'a'..'h') notation.first() - 'a' + 1 else throw IllegalArgumentException()
    val row = notation.last().toString().toIntOrNull() ?: throw IllegalArgumentException()
    return Square(column, row)
}

/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматная ладья пройдёт из клетки start в клетку end.
 * Шахматная ладья может за один ход переместиться на любую другую клетку
 * по вертикали или горизонтали.
 * Ниже точками выделены возможные ходы ладьи, а крестиками -- невозможные:
 *
 * xx.xxххх
 * xх.хxххх
 * ..Л.....
 * xх.хxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: rookMoveNumber(Square(3, 1), Square(6, 3)) = 2
 * Ладья может пройти через клетку (3, 3) или через клетку (6, 1) к клетке (6, 3).
 */
fun rookMoveNumber(start: Square, end: Square): Int {
    require(start.inside() && end.inside())
    val colMove = if (start.column == end.column) 0 else 1
    val rowMove = if (start.row == end.row) 0 else 1
    return colMove + rowMove
}

/**
 * Средняя (3 балла)
 *
 * Вернуть список из клеток, по которым шахматная ладья может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов ладьи см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: rookTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможен ещё один вариант)
 *          rookTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(3, 3), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          rookTrajectory(Square(3, 5), Square(8, 5)) = listOf(Square(3, 5), Square(8, 5))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun rookTrajectory(start: Square, end: Square): List<Square> = when (rookMoveNumber(start, end)) {
    1 -> listOf(start, end)
    2 -> listOf(start, Square(end.column, start.row), end)
    else -> listOf(start)
}

/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматный слон пройдёт из клетки start в клетку end.
 * Шахматный слон может за один ход переместиться на любую другую клетку по диагонали.
 * Ниже точками выделены возможные ходы слона, а крестиками -- невозможные:
 *
 * .xxx.ххх
 * x.x.xххх
 * xxСxxxxx
 * x.x.xххх
 * .xxx.ххх
 * xxxxx.хх
 * xxxxxх.х
 * xxxxxхх.
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если клетка end недостижима для слона, вернуть -1.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Примеры: bishopMoveNumber(Square(3, 1), Square(6, 3)) = -1; bishopMoveNumber(Square(3, 1), Square(3, 7)) = 2.
 * Слон может пройти через клетку (6, 4) к клетке (3, 7).
 */
fun bishopMoveNumber(start: Square, end: Square): Int {
    require(start.inside() && end.inside())
    return when {
        abs(start.column - end.column) % 2 != abs(start.row - end.row) % 2 -> -1
        start == end -> 0
        else -> if (abs(start.column - end.column) == abs(start.row - end.row)) 1 else 2
    }
}

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный слон может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов слона см. предыдущую задачу.
 *
 * Если клетка end недостижима для слона, вернуть пустой список.
 *
 * Если клетка достижима:
 * - список всегда включает в себя клетку start
 * - клетка end включается, если она не совпадает со start.
 * - между ними должны находиться промежуточные клетки, по порядку от start до end.
 *
 * Примеры: bishopTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          bishopTrajectory(Square(3, 1), Square(3, 7)) = listOf(Square(3, 1), Square(6, 4), Square(3, 7))
 *          bishopTrajectory(Square(1, 3), Square(6, 8)) = listOf(Square(1, 3), Square(6, 8))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun bishopTrajectory(start: Square, end: Square): List<Square> = when (bishopMoveNumber(start, end)) {
    0 -> listOf(start)
    1 -> listOf(start, end)
    2 -> listOf(start, crossSquare(start, end), end)
    else -> listOf()
}

fun crossSquare(first: Square, second: Square): Square {
    val b1 = first.row - first.column
    val b2 = second.row - second.column
    val b3 = first.row + first.column
    val b4 = second.row + second.column
    val firstCrossSquare = Square((b4 - b1) / 2, (b4 - b1) / 2 + b1)
    val secondCrossSquare = Square((b3 - b2) / 2, (b3 - b2) / 2 + b2)
    return if (firstCrossSquare.inside()) firstCrossSquare else secondCrossSquare
}

/**
 * Средняя (3 балла)
 *
 * Определить число ходов, за которое шахматный король пройдёт из клетки start в клетку end.
 * Шахматный король одним ходом может переместиться из клетки, в которой стоит,
 * на любую соседнюю по вертикали, горизонтали или диагонали.
 * Ниже точками выделены возможные ходы короля, а крестиками -- невозможные:
 *
 * xxxxx
 * x...x
 * x.K.x
 * x...x
 * xxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: kingMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Король может последовательно пройти через клетки (4, 2) и (5, 2) к клетке (6, 3).
 */
fun kingMoveNumber(start: Square, end: Square): Int = TODO()

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный король может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов короля см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: kingTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможны другие варианты)
 *          kingTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(4, 2), Square(5, 2), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          kingTrajectory(Square(3, 5), Square(6, 2)) = listOf(Square(3, 5), Square(4, 4), Square(5, 3), Square(6, 2))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun kingTrajectory(start: Square, end: Square): List<Square> = TODO()

/**
 * Сложная (6 баллов)
 *
 * Определить число ходов, за которое шахматный конь пройдёт из клетки start в клетку end.
 * Шахматный конь одним ходом вначале передвигается ровно на 2 клетки по горизонтали или вертикали,
 * а затем ещё на 1 клетку под прямым углом, образуя букву "Г".
 * Ниже точками выделены возможные ходы коня, а крестиками -- невозможные:
 *
 * .xxx.xxx
 * xxKxxxxx
 * .xxx.xxx
 * x.x.xxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: knightMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Конь может последовательно пройти через клетки (5, 2) и (4, 4) к клетке (6, 3).
 */
fun legalKnightMoves(square: Square): Set<Square> {
    val setOfMoves = mutableSetOf<Square>()
    val moveOffsets = listOf(
        (-1 to -2), (-1 to 2), (-2 to -1), (-2 to 1),
        (1 to -2), (1 to 2), (2 to -1), (2 to 1)
    )
    for ((x, y) in moveOffsets) {
        val newSquare = Square(square.column + x, square.row + y)
        if (newSquare.inside()) setOfMoves.add(newSquare)
    }
    return setOfMoves
}

fun knightGraph(): Graph {
    val g = Graph()
    for (row in 1..8) {
        for (col in 1..8) {
            val newSquare = Square(col, row)
            val newMoves = legalKnightMoves(Square(col, row))
            g.addVertex(newSquare.notation())
            for (m in newMoves) {
                g.addVertex(m.notation())
                g.connect(m.notation(), newSquare.notation())
            }
        }
    }
    return g
}

fun knightMoveNumber(start: Square, end: Square): Int {
    require(start.inside() && end.inside())
    val g = knightGraph()
    return g.bfs(start.notation(), end.notation())
}

/**
 * Очень сложная (10 баллов)
 *
 * Вернуть список из клеток, по которым шахматный конь может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов коня см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры:
 *
 * knightTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 * здесь возможны другие варианты)
 * knightTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(5, 2), Square(4, 4), Square(6, 3))
 * (здесь возможен единственный вариант)
 * knightTrajectory(Square(3, 5), Square(5, 6)) = listOf(Square(3, 5), Square(5, 6))
 * (здесь опять возможны другие варианты)
 * knightTrajectory(Square(7, 7), Square(8, 8)) =
 *     listOf(Square(7, 7), Square(5, 8), Square(4, 6), Square(6, 7), Square(8, 8))
 *
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun knightTrajectory(start: Square, end: Square): List<Square> {
    require(start.inside() && end.inside())
    val g = knightGraph()
    return g.listBfs(start.notation(), end.notation())
}

// 0 - черные, 1 - белые
fun returnBoard(situation: Map<Int, Set<String>>): String {
    val blackSquares = situation[0]
    val whiteSquares = situation[1]
    val res = StringBuilder()
    for (i in 1..8) {
        val line = StringBuilder()
        for (j in 1..8) {
            when {
                Square(j, i).notation() in blackSquares!! -> line.append("0")
                Square(j, i).notation() in whiteSquares!! -> line.append("1")
                else -> line.append("x")
            }
        }
        res.appendLine(line)
    }
    return res.toString().split("\n").reversed().joinToString("\n")
}

fun stateToSituation(state: String): Map<Int, MutableSet<String>> {
    require(state.split("\n").size == 8 && state.split("\n").filter { it.length == 8 }.size == 8)
    var row = 8
    val res = mutableMapOf(0 to mutableSetOf<String>(), 1 to mutableSetOf())
    for (line in state.split("\n")) {
        var col = 1
        for (char in line) {
            when (char) {
                '1' -> res[1]!!.add(Square(col, row).notation())
                '0' -> res[0]!!.add(Square(col, row).notation())
                'x' -> {
                    col += 1
                    continue
                }
                else -> throw IllegalArgumentException()
            }
            col++
        }
        row--
    }
    return res
}

fun checkSquare(square: Square, situation: Map<Int, MutableSet<String>>): Int {
    val whiteSquares = situation[1]
    val blackSquares = situation[0]
    return when (square.notation()) {
        in whiteSquares!! -> 1
        in blackSquares!! -> 0
        else -> -1
    }
}

fun legalMoves(start: Square, situation: Map<Int, MutableSet<String>>): Set<Square> {
    val res = mutableSetOf<Square>()
    val offset = mutableListOf(
        1 to 1, -1 to -1, 1 to -1, -1 to 1
    )
    val startState = checkSquare(start, situation)
    for ((x, y) in offset) {
        val newSquare = Square(start.column + x, start.row + y)
        val state = checkSquare(newSquare, situation)
        when {
            state == startState -> continue
            state == -1 -> res.add(newSquare)
            state != startState && state != -1 -> res.add(Square(newSquare.column + x, newSquare.row + y))
        }
    }
    return res
}

fun move(move: String, state: String): String {
    val situation = stateToSituation(state)
    require(move.split(" ").size == 2)
    val start = square(move.split(" ")[0])
    val end = square(move.split(" ")[1])
    val startColour = checkSquare(start, situation)
    require(start.inside() && end.inside())
    require(startColour != -1)
    val legalMoves = legalMoves(start, situation)
    require(end in legalMoves)
    situation[startColour]!!.add(end.notation())
    situation[startColour]!!.remove(start.notation())
    if (abs(end.row - start.row) == 2) {
        val killedSquare = Square((start.column + end.column) / 2, (start.row + end.row) / 2)
        situation[abs(startColour - 1)]!!.remove(killedSquare.notation())
    }
    return returnBoard(situation)
}

fun main() {
    val state = "xxxxxxxx\nxxxxxxxx\nxxxxxxx0\nxxxxxx1x\nxxxxxxxx\nxxxxxxxx\nxxxxxxxx\nxxxxxxxx"
    println(move("h6 g7", state))
}


