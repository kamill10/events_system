pipeline {
    agent any

    environment {
        JAVA_HOME = '/opt/java/jdk-21.0.2'
    }

    stages {
        stage('Compose down') {
            steps {
                sh 'printenv'
                echo WORKSPACE
                dir("${PWD}/docker") {
                    sh('docker compose down')
                }
            }
        }

        stage('Maven tests') {
            steps {
                dir("${PWD}") {
                    sh('mvn clean install')
                }
            }
        }

        stage('Infrastructure setup') {
            steps {
                dir("${PWD}/docker") {
                    sh('cd ../ssbd202401web/ && npm install && npm run build')
                    sh('rm -rf ./html/')
                    sh('cp -r ../ssbd202401web/dist/ html/')
                    sh('docker compose up -d --build')
                }
            }
        }

        stage('End-to-end tests') {
            steps {
              dir("${PWD}") {
                    echo 'Doing end to end test'
                    echo 'Finished end to end test'
                }
            }
        }

        stage('Infrastructure down') {
            steps {
                dir("${PWD}docker") {
                     sh('docker compose down')
                }
            }
        }
    }
}