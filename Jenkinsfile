pipeline {
    agent any

    environment {
        IMAGE_NAME = 'karanmonga/survey-app'
        DOCKER_HUB_CREDENTIALS = credentials('docker-token')
        CLUSTER_NAME = 'swe645-h3-cluster'
        CLUSTER_ZONE = 'us-east1'
        PROJECT_ID = 'concise-orb-439615-r5'
//         PATH = "/home/ubuntu/google-cloud-sdk/bin:${env.PATH}"
        USE_GKE_GCLOUD_AUTH_PLUGIN = "True"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Create Maven') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build & Push') {
            steps {
                script {
                    sh """
                        echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin
                        docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([file(credentialsId: 'gcp-service-account', variable: 'GC_KEY')]) {
                    sh """
                        gcloud auth activate-service-account --key-file=$GC_KEY
                        gcloud container clusters get-credentials swe645-h3-cluster --zone us-east1 --project concise-orb-439615-r5
                        kubectl apply -f deployment.yaml

                    """
                }


            }
        }


    }
}
