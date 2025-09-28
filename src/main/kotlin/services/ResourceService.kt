package services

import models.Permission
import models.Resource

class ResourceService {
    private val resources = mutableListOf<Resource>()

    init {
        // Предустановленные ресурсы
        resources.addAll(listOf(
            Resource("A", 100, mapOf(
                "alice" to Permission(read = true, write = false, execute = false),
                "bob" to Permission(read = true, write = true, execute = false)
            )),
            Resource("A.B", 50, mapOf(
                "alice" to Permission(read = true, write = true, execute = false)
            )),
            Resource("A.B.C", 20, mapOf(
                "admin" to Permission(read = true, write = true, execute = true)
            )),
            Resource("A.B.D", 30),
            Resource("X", 200, mapOf(
                "bob" to Permission(read = true, write = false, execute = true)
            )),
            Resource("X.Y", 100)
        ))
    }

    fun checkAccess(login: String, resourcePath: String, action: String, volume: Int): Int {
        // Проверка формата ресурса
        if (!resourcePath.matches(Regex("^[A-Za-z0-9_]{1,20}(\\.[A-Za-z0-9_]{1,20})*$"))) {
            return 7 // Некорректный формат ресурса
        }

        // Проверка объема
        if (volume < 0) {
            return 7 // Некорректный формат объема
        }

        // Поиск ресурса и проверка наследования прав
        val resource = findResourceWithInheritance(resourcePath, login)
        if (resource == null) {
            return 6 // Несуществующий ресурс
        }

        // Проверка объема
        if (volume > resource.maxVolume) {
            return 8 // Превышение максимального объема
        }

        // Проверка прав доступа
        val permission = resource.permissions[login] ?: return 5 // Нет доступа

        return when (action) {
            "read" -> if (permission.read) 0 else 5
            "write" -> if (permission.write) 0 else 5
            "execute" -> if (permission.execute) 0 else 5
            else -> 4 // Неизвестное действие
        }
    }

    private fun findResourceWithInheritance(resourcePath: String, login: String): Resource? {
        var currentPath = ""
        var inheritedPermission: Permission? = null
        var targetResource: Resource? = null

        for (part in resourcePath.split(".")) {
            currentPath = if (currentPath.isEmpty()) part else "$currentPath.$part"
            val resource = resources.find { it.path == currentPath }

            if (resource != null) {
                val currentPermission = resource.permissions[login]
                if (currentPermission != null) {
                    inheritedPermission = currentPermission
                }
                if (currentPath == resourcePath) {
                    targetResource = resource
                }
            }
        }

        return targetResource?.copy(
            permissions = targetResource.permissions +
                    (login to (targetResource.permissions[login] ?: inheritedPermission ?: return null))
        )
    }
}