package com.pedro.jenkins

class ShellResourceRunner implements Serializable {
    private final def steps

    ShellResourceRunner(def steps) {
        this.steps = steps
    }

    Object run(String resourcePath, Map options = [:]) {
        if (!resourcePath) {
            throw new IllegalArgumentException('resourcePath is required')
        }

        def returnStdout = options.get('returnStdout', false) as boolean
        def returnStatus = options.get('returnStatus', false) as boolean
        if (returnStdout && returnStatus) {
            throw new IllegalArgumentException('returnStdout and returnStatus cannot both be true')
        }

        //Only relevant when returnStdout: true; echoes captured output to Jenkins log.
        def printStdout = options.get('printStdout', true) as boolean

        //If true, runs script with bash -x to show executed commands (+ ...) in logs.
        def trace = options.get('trace', false) as boolean

        def cleanup = options.get('cleanup', true) as boolean
        def args = options.get('args', [])
        def env = options.get('env', [:])

        String fileName = options.get('fileName') as String
        if (!fileName) {
            fileName = "lib-${UUID.randomUUID().toString().replace('-', '')}.sh"
        }

        String scriptContent = steps.libraryResource(resourcePath)
        steps.writeFile file: fileName, text: scriptContent

        Object result = null
        try {
            String escapedFileName = shellEscape(fileName)
            steps.sh "chmod +x ${escapedFileName}"

            String command = trace
                    ? "bash -x ${shellEscape("./${fileName}")}${formatArgs(args)}"
                    : "./${fileName}${formatArgs(args)}"
            if (env instanceof Map && !env.isEmpty()) {
                List<String> envList = env.collect { k, v -> "${k}=${v}" }
                result = steps.withEnv(envList) {
                    runSh(command, returnStdout, returnStatus, printStdout)
                }
            } else {
                result = runSh(command, returnStdout, returnStatus, printStdout)
            }
        } finally {
            if (cleanup) {
                steps.sh "rm -f ${shellEscape(fileName)}"
            }
        }

        return result
    }

    private Object runSh(String command, boolean returnStdout, boolean returnStatus, boolean printStdout) {
        if (returnStdout) {
            String output = steps.sh(script: command, returnStdout: true)
            String trimmed = output.trim()
            if (printStdout && trimmed) {
                steps.echo(trimmed)
            }
            return trimmed
        }
        if (returnStatus) {
            return steps.sh(script: command, returnStatus: true)
        }
        steps.sh command
        return null
    }

    private static String formatArgs(def args) {
        if (args == null) {
            return ''
        }
        if (args instanceof String) {
            return " ${args}"
        }
        if (args instanceof List) {
            return args.isEmpty() ? '' : " ${args.collect { shellEscape(it?.toString()) }.join(' ')}"
        }
        return " ${shellEscape(args.toString())}"
    }

    private static String shellEscape(String value) {
        return "'${value.replace("'", "'\"'\"'")}'"
    }
}
