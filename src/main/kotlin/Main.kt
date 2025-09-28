import models.*
import services.*
import utils.ArgsParser

fun main(args: Array<String>) {
    val argsParser = ArgsParser(args)

    if (argsParser.shouldShowHelp()) {
        argsParser.showHelp()
        kotlin.system.exitProcess(1)
    }

    if (!argsParser.validateArgs()) {
        argsParser.showHelp()
        kotlin.system.exitProcess(1)
    }

    val authService = AuthService(HashService())
    val resourceService = ResourceService()
    
    val authResult = authService.authenticate(
        argsParser.login!!,
        argsParser.password!!
    )

    if (authResult != 0) {
        kotlin.system.exitProcess(authResult)
    }
    
    val accessResult = resourceService.checkAccess(
        argsParser.login!!,
        argsParser.resource!!,
        argsParser.action!!,
        argsParser.volume!!
    )

    kotlin.system.exitProcess(accessResult)
}
