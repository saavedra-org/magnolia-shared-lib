@Library('jenkins-shared-library') _

pipeline {
    agent any

    stages {
        stage('Download from Nexus') {
            steps {
                script {
                    downloadFromNexus(
                            nexusUrl: 'http://127.0.0.1:45801',
                            repo: 'maven-releases',
                            groupId: 'com.pedro.jenkins',
                            artifactId: 'jenkins-shared-library',
                            version: '1.0.0',
                            credsId: 'nexus-creds',
                            dest: 'artifacts/jenkins-shared-library-1.0.0.jar'
                    )
                }
            }
        }

        stage('Run Example') {
            steps {
                script {
                    runShell('scripts/helloWorld.sh', [returnStdout: true, trace: true])
                }
            }
        }
    }
}
