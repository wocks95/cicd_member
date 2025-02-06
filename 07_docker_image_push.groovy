import java.text.SimpleDateFormat
import java.util.Date

def NOW = (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())

pipeline {
    agent any
    environment {
        dockerTag = "${NOW}_${BUILD_ID}"
        dockerImage = "minkt78/cicd_member:${dockerTag}"
    }
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
        stage("Docker Image Build") {
            steps {
                script {
                    oDockImage = docker.build(dockerImage, "--build-arg VERSION=${dockerTag} -f Dockerfile .")
                }
            }
        }
        stage("Docker Image Push") {
            steps {
                script {
                    docker.withRegistry("", "DockerHub_Credential") {
                        oDockImage.push()
                    }
                }
            }
        }
    }
}