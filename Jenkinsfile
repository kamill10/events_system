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
                dir("${WORKSPACE}/docker") {
                    sh('docker compose down')
                }
            }
        }

        stage('Maven tests') {
            steps {
                dir("${WORKSPACE}") {
                    sh('mvn clean install')
                }
            }
        }

        stage('Infrastructure setup') {
            steps {
                dir("${WORKSPACE}/docker") {
                    sh('cd ../ssbd202401web/ && npm install && npm run build')
                    sh('rm -rf ./html/')
                    sh('cp -r ../ssbd202401web/dist/ html/')
                    sh('docker compose up -d --build')
                }
            }
        }

        stage('End-to-end tests') {
            steps {
              dir("${WORKSPACE}") {
                    echo 'Doing end to end test'
                    echo 'Finished end to end test'
                }
            }
        }

        stage('Infrastructure down') {
            steps {
                dir("${WORKSPACE}docker") {
                     sh('docker compose down')
                }
            }
        }
    }
}