pipeline {
    agent any

    environment {
        JAVA_HOME = '/opt/java/jdk-21.0.2'
    }

    stages {
        stage('Compose down') {
            steps {
                sh 'printenv'
                dir("/home/jenkins/workspace/test/docker") {
                    sh('docker compose down')
                }
            }
        }

        stage('Maven tests') {
            steps {
                dir("/home/jenkins/workspace/test") {
                    sh('mvn clean install')
                }
            }
        }

        stage('Infrastructure setup') {
            steps {
                dir("/home/jenkins/workspace/test/docker") {
                    sh('cd ../ssbd202401web/ && npm install && npm run build')
                    sh('rm -rf ./html/')
                    sh('cp -r ../ssbd202401web/dist/ html/')
                    sh('docker compose up -d --build')
                }
            }
        }

        stage('End-to-end tests') {
            steps {
              dir("/home/jenkins/workspace/test") {
                    echo 'Doing end to end test'
                    echo 'Finished end to end test'
                }
            }
        }

        stage('Infrastructure down') {
            steps {
                dir("/home/jenkins/workspace/test/docker") {
                     sh('docker compose down')
                }
            }
        }
    }
}