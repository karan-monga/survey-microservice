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
                checkout scm
            }
        }

        stage('Create Maven') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build & Push Docker Hub') {
            steps {
                script {
                    sh """
                        echo "Logging into Docker Hub..."
                        echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin

                        echo "Building Docker image..."
                        docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} .

                        echo "Pushing image to Docker Hub..."
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
                        gcloud auth activate-service-account --key-file="$GC_KEY"
                        gcloud config set project ${PROJECT_ID}

                        echo "Configuring kubectl..."
                        gcloud container clusters get-credentials ${CLUSTER_NAME} --zone ${CLUSTER_ZONE}

                        echo "Updating deployment image to version ${BUILD_NUMBER}..."
                        sed -i "s|image: gcr.io/concise-orb-439615-r5/survey-app:.*|image: gcr.io/concise-orb-439615-r5/survey-app:${BUILD_NUMBER}|g" deployment.yaml

                        echo "Applying Kubernetes manifests..."
                        kubectl apply -f deployment.yaml
                        kubectl apply -f service.yaml



                    '''
                }
            }
        }
    }

}