import com.pedro.jenkins.ShellResourceRunner

def call(String filePath, Map options = [:]) {
    def runner = new ShellResourceRunner(this)
    runner.run(filePath, options)
}
