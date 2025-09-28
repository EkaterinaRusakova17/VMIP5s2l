package models

data class Resource(
    val path: String,
    val maxVolume: Int,
    val permissions: Map<String, Permission> = emptyMap()
) {
    fun isValidPath(): Boolean {
        return path.matches(Regex("^[A-Za-z0-9_]{1,20}(\\.[A-Za-z0-9_]{1,20})*$"))
    }
}