def call() {
    pipeline {
        agent any

        stages {
            stage('Run Shell From Library') {
                steps {
                    runExampleScript()
                }
            }
        }
    }
}