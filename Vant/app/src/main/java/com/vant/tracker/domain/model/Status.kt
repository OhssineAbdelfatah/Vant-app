package com.vant.tracker.domain.model

enum class Status { PLANNED, WATCHING, COMPLETED, DROPPED }

fun Status.displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }
