import groovy.transform.Field

@Field String scriptName = 'default.sh'
@Field List<String> scriptArgs = []

def executeScript(String scriptPath, List<String> args = []) {
    scriptName = scriptPath
    scriptArgs = args
    
    // Load the script from resources
    def scriptContent = this.getClass().getResource('/' + scriptName).text
    def tempFile = File.createTempFile('jenkins_script', '.sh')
    tempFile.text = scriptContent
    tempFile.setExecutable(true)
    
    try {
        // Execute the temporary script
        def process = "${tempFile.absolutePath} ${scriptArgs.join(' ')}".execute() 
        process.in.eachLine { line -> 
            println line 
        }
        process.waitFor()
        if (process.exitValue() != 0) {
            throw new RuntimeException('Script execution failed')
        }
    } finally {
        // Clean up the temporary file
        tempFile.delete() 
    }
}
