package utils

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.optional

class ArgsParser(args: Array<String>) {
    private val parser = ArgParser("ResourceAccessApp")

    val login by parser.option(ArgType.String, shortName = "l", fullName = "login").optional()
    val password by parser.option(ArgType.String, shortName = "p", fullName = "password").optional()
    val resource by parser.option(ArgType.String, shortName = "r", fullName = "resource").optional()
    val action by parser.option(ArgType.String, shortName = "a", fullName = "action").optional()
    val volume by parser.option(ArgType.Int, shortName = "v", fullName = "volume").optional()
    val help by parser.option(ArgType.Boolean, shortName = "h", fullName = "help").optional()

    init {
        try {
            parser.parse(args)
        } catch (e: Exception) {
        }
    }

    fun shouldShowHelp(): Boolean {
        return help == true ||
                login == null && password == null && resource == null &&
                action == null && volume == null && help == null
    }

    fun showHelp() {
        println("""
            Resource Access Control System
            
            Usage: java -jar app.jar [OPTIONS]
            
            Options:
            -l, --login LOGIN      User login
            -p, --password PASS    User password  
            -r, --resource PATH    Resource path (e.g., A.B.C)
            -a, --action ACTION    Action to perform (read/write/execute)
            -v, --volume VOLUME    Requested resource volume
            -h, --help            Show this help message
            
            Exit codes:
            0 - Success
            1 - Help requested
            2 - Invalid password
            3 - Invalid login
            4 - Unknown action
            5 - Access denied
            6 - Resource not found
            7 - Invalid resource format or volume
            8 - Volume exceeded
        """.trimIndent())
    }

    fun validateArgs(): Boolean {
        return when {
            help == true -> true
            login == null || password == null || resource == null ||
                    action == null || volume == null -> false
            else -> true
        }
    }
}