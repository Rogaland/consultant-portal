pipeline {
    agent { label 'docker' }
    stages {
        stage('Build') {
            steps {
                sh 'git clean -fdx'
                sh "docker build -t ${GIT_COMMIT} ."
            }
        }
    }
}
