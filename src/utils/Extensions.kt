package es.jtp.kterm.utils

fun String.stringify() =
        this.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\"", "\\\"")
