#!groovy

node {
    currentBuild.result = "SUCCESS"

    try {
        stage('checkout') {
            checkout scm
        }

        stage('build') {
            sh './gradlew'
        }

        stage('deploy') {
            //sh 'chmod +x docker-build'
            //withCredentials([string(credentialsId: 'rogfkConsultantPortalRunParams', variable: 'runParams')]) {
            //    sh 'sudo -E sh ./docker-build'
            // }
        }
    }

    catch (err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}