package com.example.yervand.ftstest.utill

object QueryBuilder {

    fun createQuery(key: String): String {
        val regex = Regex(EXP)
        val keysList = regex.split(key)
                .filter { it -> it != "" }
        return if (keysList.size > 1) {
            keysList.asSequence()
                    .map { it -> "$it*" }
                    .joinToString(AND)
        } else {
            if (!key.isEmpty()) {
                "${key.trim()}*"
            } else {
                key
            }
        }
    }

    private const val AND = " AND "
    private const val EXP = "[^ա-ֆԱ-Ֆ0-9]"
}