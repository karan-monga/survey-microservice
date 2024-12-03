pipeline {
    agent any

    environment {
        IMAGE_NAME = 'karanmonga/survey-app'
        DOCKER_REGISTRY = 'docker.io'
        GCR_REGISTRY = 'gcr.io/concise-orb-439615-r5/survey-app'
        DOCKER_HUB_CREDENTIALS = credentials('docker-token')
        CLUSTER_NAME = 'swe645-h3-cluster'
        CLUSTER_ZONE = 'us-east1'
        PROJECT_ID = 'concise-orb-439615-r5'
        USE_GKE_GCLOUD_AUTH_PLUGIN = "True"
    }

    stages {
        stage('Checkout') {
            steps {
                // Pull the source code from the repository
                checkout scm
            }
        }

        stage('Create Maven') {
            steps {
                // Build and test the Java project using Maven
                sh 'mvn clean install'
            }
        }

        stage('Build & Push Docker Hub') {
            steps {
                script {
                    sh """
                        echo "Logging into Docker Hub..."
                        // Authenticate Docker with stored credentials
                        echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin

                        echo "Building Docker image..."
                        // Build Docker image with a unique tag using the BUILD_NUMBER
                        docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .

                        echo "Pushing image to Docker Hub..."
                        // Push the image to Docker Hub
                        docker push ${IMAGE_NAME}:${BUILD_NUMBER}
                    """
                }
            }
        }

        stage('Deploy to GKE') {
            steps {
                withCredentials([file(credentialsId: 'gcp-service-account', variable: 'GC_KEY')]) {
                    sh '''
                        set -e  # Exit immediately if a command exits with a non-zero status

                        echo "Authenticating with Google Cloud..."
                        // Use the service account key file for GCP authentication
                        gcloud auth activate-service-account --key-file="$GC_KEY"
                        gcloud config set project ${PROJECT_ID}

                        echo "Configuring kubectl..."
                        // Fetch cluster credentials to interact with GKE
                        gcloud container clusters get-credentials ${CLUSTER_NAME} --zone ${CLUSTER_ZONE}

                        // Update the deployment with the new Docker image
                        kubectl set image deployment/survey-app survey-app=${IMAGE_NAME}:${BUILD_NUMBER} --record
                    '''
                }
            }
        }
    }

}
