package models

data class Permission(
    val read: Boolean = false,
    val write: Boolean = false,
    val execute: Boolean = false
)