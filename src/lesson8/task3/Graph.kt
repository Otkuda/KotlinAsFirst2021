package lesson8.task3

import lesson8.task2.Square
import lesson8.task2.square
import java.util.*

class Graph {
    private data class Vertex(val name: String) {
        val neighbors = mutableSetOf<Vertex>()
    }

    private val vertices = mutableMapOf<String, Vertex>()

    private operator fun get(name: String) = vertices[name] ?: throw IllegalArgumentException()

    fun addVertex(name: String) {
        if (name !in vertices) vertices[name] = Vertex(name)
    }

    private fun connect(first: Vertex, second: Vertex) {
        first.neighbors.add(second)
        second.neighbors.add(first)
    }

    fun connect(first: String, second: String) = connect(this[first], this[second])

    fun neighbors(name: String) = vertices[name]?.neighbors?.map { it.name } ?: listOf()

    /**
     * Пример
     *
     * По двум вершинам рассчитать расстояние между ними = число дуг на самом коротком пути между ними.
     * Вернуть -1, если пути между вершинами не существует.
     *
     * Используется поиск в ширину
     */
    fun bfs(start: String, finish: String) = bfs(this[start], this[finish])

    private fun bfs(start: Vertex, finish: Vertex): Int {
        val queue = ArrayDeque<Vertex>()
        queue.add(start)
        val visited = mutableMapOf(start to 0)
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            val distance = visited[next]!!
            if (next == finish) return distance
            for (neighbor in next.neighbors) {
                if (neighbor in visited) continue
                visited[neighbor] = distance + 1
                queue.add(neighbor)
            }
        }
        return -1
    }

    fun listBfs(start: String, finish: String) = listBfs(this[start], this[finish])

    private fun listBfs(start: Vertex, finish: Vertex): List<Square> {
        val queue = ArrayDeque<Vertex>()
        queue.add(start)
        val visited = mutableMapOf(start to mutableListOf(square(start.name)))
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            val list = visited[next]!!
            if (next == finish) return list
            for (neighbor in next.neighbors) {
                if (neighbor in visited) continue
                list.add(square(neighbor.name))
                visited[neighbor] = list
                queue.add(neighbor)
            }
        }
        return listOf()
    }

    /**
     * Пример
     *
     * По двум вершинам рассчитать расстояние между ними = число дуг на самом коротком пути между ними.
     * Вернуть -1, если пути между вершинами не существует.
     *
     * Используется поиск в глубину
     */
    fun dfs(start: String, finish: String): Int = dfs(this[start], this[finish], setOf()) ?: -1

    private fun dfs(start: Vertex, finish: Vertex, visited: Set<Vertex>): Int? =
        if (start == finish) 0
        else {
            val min = start.neighbors
                .filter { it !in visited }
                .mapNotNull { dfs(it, finish, visited + start) }
                .minOrNull()
            if (min == null) null else min + 1
        }
}


fun main() {
    val graph = Graph()
    graph.addVertex("A")
    graph.addVertex("B")
    graph.addVertex("C")
    graph.addVertex("D")
    graph.addVertex("E")
    graph.addVertex("F")
    graph.addVertex("G")
    graph.addVertex("H")
    graph.connect("A", "B")
    println(graph)
}