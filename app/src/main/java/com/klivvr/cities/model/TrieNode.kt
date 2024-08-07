package com.klivvr.cities.model

class TrieNode {
    val children: MutableMap<Char, TrieNode> = mutableMapOf()
    var isEndOfWord: Boolean = false
    var city: CityItem? = null
}

class Trie {
    private val root = TrieNode()

    fun insert(city: CityItem) {
        var currentNode = root
        for (char in city.name) {
            currentNode = currentNode.children.getOrPut(char) { TrieNode() }
        }
        currentNode.isEndOfWord = true
        currentNode.city = city
    }

    fun search(prefix: String): ArrayList<CityItem> {
        var currentNode = root
        for (char in prefix) {
            currentNode = currentNode.children[char] ?: return arrayListOf()
        }
        return collectAllWords(currentNode)
    }

    private fun collectAllWords(node: TrieNode): ArrayList<CityItem> {
        val result = arrayListOf<CityItem>()
        if (node.isEndOfWord) {
            node.city?.let { result.add(it) }
        }
        for ((_, childNode) in node.children) {
            result.addAll(collectAllWords(childNode))
        }
        return result
    }
}

fun buildTrie(cities: City): Trie {
    val trie = Trie()
    for (city in cities) {
        trie.insert(city)
    }
    return trie
}

fun searchCities(trie: Trie, prefix: String): ArrayList<CityItem> {
    return trie.search(prefix)
}
