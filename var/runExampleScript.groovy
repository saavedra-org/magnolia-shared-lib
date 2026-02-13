def call() {
    // Load script from resources
    def scriptContent = libraryResource('scripts/example.sh')

    // Write to workspace
    writeFile file: 'example.sh', text: scriptContent

    // Make executable
    sh 'chmod +x example.sh'

    // Execute
    sh './example.sh'
}