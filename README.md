# Jenkins Shared Library

This repository provides Jenkins shared library steps plus shell scripts packaged under `resources/`.

## Usage
Example Jenkinsfile:

```groovy
@Library('jenkins-shared-library') _

pipeline {
    agent any
    stages {
        stage('Example') {
            steps {
                runExampleScript()
            }
        }
    }
}
```