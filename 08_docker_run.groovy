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
                // github 레파지토리가 private 인 경우 Github_Credential 크레덴셜 추가 후 아래와 같이 사용하면 됩니다. (username: github 아이디, password: personal access token)
                // git (branch: "main", url: "https://github.com/teacher-min/cicd_member.git", credentialsId: "Github_Credential")
                git (branch: "main", url: "https://github.com/teacher-min/cicd_member.git", credentialsId: "Github_Credential")
            }
        }
        stage("Build") {
            steps {
                sh "chmod +x ./mvnw"
                sh "./mvnw clean package -Dspring.datasource.url=jdbc:mysql://3.105.240.39:3306/db_cicd_member -Dspring.datasource.username=greenit -Dspring.datasource.password=greenit"
            }
            post {
                success {
                    archiveArtifacts "target/*.jar"
                }
            }
        }
        stage("JUnit Test") {
            steps {
                sh "./mvnw test -Dspring.datasource.url=jdbc:mysql://3.105.240.39:3306/db_cicd_member -Dspring.datasource.username=greenit -Dspring.datasource.password=greenit"
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
        stage("Deploy") {
            steps {
                sshagent(credentials: ["Staging_Credential"]) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@172.31.32.100 docker rm -f memberapp-container"
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@172.31.32.100 docker run -d --name memberapp-container \
                                                                                -p 9090:8080 \
                                                                                -e MYSQL_IP=3.105.240.39 \
                                                                                -e MYSQL_PORT=3306 \
                                                                                -e MYSQL_DATABASE=db_cicd_member \
                                                                                -e MYSQL_USERNAME=greenit \
                                                                                -e MYSQL_PASSWORD=greenit \
                                                                                ${dockerImage}"
                }
            }
        }
    }
}