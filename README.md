# Magnolia Shared Library

## Project Documentation

The Magnolia Shared Library is designed to provide reusable pipeline code for Jenkins that can be shared across multiple projects.

## Setup Instructions

1. **Install Jenkins**: Ensure you have Jenkins installed on your server.
2. **Load the Shared Library**:
   - Navigate to `Manage Jenkins` > `Configure System`.
   - Under the `Global Pipeline Libraries` section, add a new library:
     - Name: `magnolia-shared-lib`
     - Source Code Management: Choose 'Git' and provide the repository URL: `https://github.com/saavedra-org/magnolia-shared-lib`
     - Specify the branch to use (default: `main`).
3. **Configure Access**: Ensure Jenkins has permissions to access the repository. If using a private repo, you may need to add credentials.

## Usage Examples

To use the Magnolia Shared Library in your Jenkins pipeline, include it in your `Jenkinsfile`:

```groovy
@Library('magnolia-shared-lib') _
pipeline {
    agent any
    stages {
        stage('Example') {
            steps {
                echo 'Using the shared library!'
            }
        }
    }
}
```

## Pipeline Examples

Here is a simple example of a Jenkins pipeline that utilizes functions from the Magnolia Shared Library:

```groovy
@Library('magnolia-shared-lib') _
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                script {
                    // Use a function from the library
                    libraryFunction()
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    // Run tests
                    runTests()
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Deploy application
                    deployApplication()
                }
            }
        }
    }
}
```

## Conclusion

This README provides the basic guidelines for setting up and using the Magnolia Shared Library in Jenkins. For further information, please refer to the official Jenkins documentation.