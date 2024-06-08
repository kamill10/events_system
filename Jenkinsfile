pipeline {
    agent any

    environment {
        JAVA_HOME = '/opt/java/jdk-21.0.2'
    }

    stages {
//         stage('Compose down') {
//             steps {
//                 dir("${WORKSPACE}/docker-test") {
//                     sh('docker compose down')
//                 }
//             }
//         }

        stage('Maven tests') {
            steps {
                dir("${WORKSPACE}") {
                    sh('mvn clean install')
                }
            }
        }

        stage('Frontend build') {
            steps {
                dir("${WORKSPACE}/docker-test") {
                    sh('cd ../ssbd202401web/ && npm install && npm run build')
                }
            }
        }

//         stage('End-to-end tests') {
//             steps {
//               dir("${WORKSPACE}") {
//                     echo 'Doing end to end test'
//                     echo 'Finished end to end test'
//                 }
//             }
//         }

//         stage('Infrastructure down') {
//             steps {
//                 dir("${WORKSPACE}/docker-test") {
//                      sh('docker compose down')
//                 }
//             }
//         }
    }
}