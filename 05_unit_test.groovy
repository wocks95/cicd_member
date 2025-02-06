pipeline {
    agent any
    stages {
        stage("Checkout") {
            steps {
                git (branch: "main", url: "https://github.com/teacher-min/cicd_member.git")
            }
        }
        stage("Build") {
            steps {
                sh "chmod +x ./mvnw"
                sh "./mvnw clean package -Dmaven.test.failure.ignore=true"
            }
            post {
                success {
                    archiveArtifacts "target/*.jar"
                }
            }
        }
        stage("JUnit Test") {
            steps {
                sh "./mvnw test"
            }
            post {
                always {
                    junit "target/surefire-reports/TEST-*.xml"
                }
            }
        }
    }
}