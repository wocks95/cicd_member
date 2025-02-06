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
                sh "./mvnw clean package"
            }
            post {
                success {
                    archiveArtifacts "target/*.jar"
                }
            }
        }
    }
}