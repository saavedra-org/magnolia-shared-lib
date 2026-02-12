// vars/buildScript.groovy - Wrapper for build operations

def executeBuild(script) {
    return script.executeScript()
}

return this.executeBuild