package com.chimber.debtislav.util

data class Edge<T>(val a: T, val b: T, var weight: Int)

class Graph<T>(edgesList: List<Edge<T>>) {
    val edges: MutableList<Edge<T>>
    private var start: T? = null

    init {
        val edgeMap = HashMap<Pair<T, T>, Int>()
        for (edge in edgesList) {
            edgeMap.compute(edge.a to edge.b) { _, v -> edge.weight + (v ?: 0) }
        }
        edges = edgeMap.map { Edge(it.key.first, it.key.second, it.value) }.toMutableList()
    }

    private fun dfs(v: T, g: Map<T, List<Edge<T>>>, used: MutableMap<T, Int>): MutableList<Edge<T>>? {
        used[v] = 1
        val outEdges = g[v] ?: run {
            used[v] = 2
            return null
        }
        for (edge in outEdges) {
            val x = edge.b
            if (used[x] == 0) {
                val cycle = dfs(x, g, used)
                cycle?.let {
                    if (start == null) return it
                    else if (start == v) start = null
                    it.add(edge)
                    return it
                }
            } else if (used[x] == 1) {
                start = x
                return mutableListOf(edge)
            }
        }
        used[v] = 2
        return null
    }

    private fun findCycle(): List<Edge<T>> {
        val g = mutableMapOf<T, MutableList<Edge<T>>>()
        for (edge in edges) {
            if (!g.contains(edge.a)) {
                g[edge.a] = mutableListOf()
            }
            g[edge.a]?.add(edge)
        }
        val vertices = edges.map { listOf(it.a, it.b) }.flatten().distinct()

        val used = vertices.associateWith { 0 }.toMutableMap()
        start = null
        for (v in vertices) {
            if (used[v] == 0) {
                val cycle = dfs(v, g, used)
                if (cycle != null) {
                    return cycle.reversed()
                }
            }
        }
        return emptyList()
    }

    private fun reduceCycle(): Boolean {
        val cycle = findCycle()
        val minWeight = cycle.minByOrNull { it.weight }?.weight ?: return false
        val edgesToRemove = mutableListOf<Edge<T>>()
        for (edge in cycle) {
            edge.weight -= minWeight
            if (edge.weight == 0) {
                edgesToRemove.add(edge)
            }
        }
        edges.removeAll(edgesToRemove)
        return true
    }

    fun reduceAllCycles() {
        var finished = false
        while (!finished) {
            finished = !reduceCycle()
        }
    }
}
