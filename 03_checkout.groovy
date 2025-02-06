pipeline {
    agent any
    stages {
        stage("Checkout") {
            steps {
                git (branch: "main", url: "https://github.com/teacher-min/cicd_member.git")
            }
        }
    }
}