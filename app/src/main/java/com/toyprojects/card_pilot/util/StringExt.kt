package com.toyprojects.card_pilot.util

/// 글자 단위 줄바꿈을 위해 문자열의 모든 문자 사이에 \u200B 를 삽입
fun String.applyCharacterBreak(): String {
    if (this.isEmpty()) return this
    val builder = StringBuilder(this.length * 2 - 1)
    for (i in this.indices) {
        builder.append(this[i])
        if (i < this.lastIndex) {
            builder.append('\u200B')
        }
    }
    return builder.toString()
}
