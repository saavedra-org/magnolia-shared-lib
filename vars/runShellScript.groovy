def runShellScript(scriptName, args = [], scriptDir = '') {
    def scriptPath = "resources/${scriptName}"
    if (scriptDir) {
        scriptPath = "${scriptDir}/${scriptName}"
    }
    return sh(script: scriptPath, args: args)
}