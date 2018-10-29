package com.example.yervand.ftstest.db.model

enum class CodexEntityType(var value: Int) {
    Content(0),
    Codex(1), //Օրենսգիրք - Codex
    Part(2), //ՄԱՍ 1 - Part
    Section(3), //ԲԱԺԻՆ 1 - Section
    Chapter(4), //ԳԼՈՒԽ 1 - Chapter -
    Article(5), //ՀՈԴՎԱԾ 1. - Article - 1.1.
    ArticlePart(6), //ՄԱՍ 1․ - ArticlePart - 1.1.
    Paragraph(7), //ԿԵՏ 1) - Paragraph - 1.1)
    SubParagraph(8) //ԵՆԹԱԿԵՏ ա․ - SubParagraph - ա1․
}