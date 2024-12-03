# SWE 645 - HW3 - Student Survey Microservice Application

## Introduction

This project is a microservices-based application developed using **Spring Boot**, **RESTful Web Services**, and **JPA/Hibernate**. It leverages **Amazon Relational Database Service (Amazon RDS) with MySQL** to implement CRUD operations for managing student survey data. The application allows prospective students to:

- Provide survey feedback about their campus visit.
- View all recorded surveys to date.
- Update and delete specific surveys.

The application is containerized using **Docker** and deployed on **Google Kubernetes Engine (GKE)** using a **CI/CD pipeline** established with **Jenkins**.

---

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
    - [1. Clone the Repository](#1-clone-the-repository)
    - [2. Configure the Database](#2-configure-the-database)
    - [3. Build the Application](#3-build-the-application)
    - [4. Containerize the Application](#4-containerize-the-application)
    - [5. Set Up Jenkins CI/CD Pipeline](#5-set-up-jenkins-cicd-pipeline)
    - [6. Deploy to Kubernetes](#6-deploy-to-kubernetes)
- [Running the Application](#running-the-application)
- [Testing with Postman](#testing-with-postman)
- [CI/CD Pipeline Details](#cicd-pipeline-details)
- [Deployment on Kubernetes](#deployment-on-kubernetes)
- [References](#references)
- [Video Demonstration](#video-demonstration)
- [Application URL](#application-url)
- [Note](#note)
- [License](#license)

---

## Features

- **Create Survey**: Prospective students can submit survey feedback.
- **Read Surveys**: View all surveys recorded to date.
- **Update Survey**: Modify existing survey entries.
- **Delete Survey**: Remove specific survey entries.
- **RESTful API**: Interact with the application using RESTful endpoints.

---

## Technologies Used

- **Java 11**
- **Spring Boot**
- **Spring Data JPA (Hibernate)**
- **MySQL (Amazon RDS)**
- **Docker**
- **Kubernetes (GKE)**
- **Jenkins**
- **Google Cloud Platform (GCP)**
- **Postman**

---

## Prerequisites

- **Java JDK 11** or higher
- **Maven** installed
- **Docker** installed
- **Google Cloud SDK (gcloud)**
- **kubectl** command-line tool
- **Jenkins** installed and configured
- **An AWS account** with Amazon RDS access
- **A GCP account** with Kubernetes Engine API enabled
- **Postman** for API testing

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/survey-app.git
cd survey-app
```

### 2. Configure the Database

Create a MySQL database using Amazon RDS:

- Launch an RDS MySQL Instance:
  - Use the AWS Management Console to create a new MySQL instance.
  - Choose Development and Testing to avoid unexpected charges.
  - Note the endpoint, port, username, and password.
  

- Configure Application Properties:  
Update src/main/resources/application.properties with your RDS database details:

```bash
spring.datasource.url=jdbc:mysql://your-rds-endpoint:3306/your-database-name
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```

### 3. Build the Application

Ensure Maven and Java 11 are installed.

```bash
mvn clean install
```

### 4. Containerize the Application

Create a Dockerfile in the root directory:

```bash
# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the jar file into the container
COPY target/survey-app.jar survey-app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "survey-app.jar"]
```

Build the Docker image:

```bash
docker build -t your-dockerhub-username/survey-app:latest .
```

Push the image to Docker Hub:

```bash
docker login -u your-dockerhub-username -p your-dockerhub-password
docker push your-dockerhub-username/survey-app:latest
```

# 5. Set Up Jenkins CI/CD Pipeline

Install **Jenkins** and required plugins:

- **Maven Integration Plugin**
- **Docker Pipeline Plugin**
- **Google Kubernetes Engine Plugin**

Configure Jenkins Credentials:

- **Docker Hub Credentials** (`docker-token`)
- **GCP Service Account Key** (`gcp-service-account`)

Create a `Jenkinsfile` in the root directory:

```groovy
pipeline {
    agent any

    environment {
        IMAGE_NAME = 'gcr.io/your-gcp-project-id/survey-app'
        DOCKER_HUB_CREDENTIALS = credentials('docker-token')
        CLUSTER_NAME = 'your-gke-cluster-name'
        CLUSTER_ZONE = 'your-gke-cluster-zone'
        PROJECT_ID = 'your-gcp-project-id'
        USE_GKE_GCLOUD_AUTH_PLUGIN = "True"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build & Push to GCR') {
            steps {
                withCredentials([file(credentialsId: 'gcp-service-account', variable: 'GC_KEY')]) {
                    sh '''
                        echo "Authenticating with Google Cloud..."
                        gcloud auth activate-service-account --key-file="$GC_KEY"
                        gcloud config set project ${PROJECT_ID}

                        echo "Configuring Docker to use gcloud as a credential helper..."
                        gcloud auth configure-docker

                        echo "Building Docker image..."
                        docker build -t gcr.io/${PROJECT_ID}/survey-app:${BUILD_NUMBER} .

                        echo "Pushing image to GCR..."
                        docker push gcr.io/${PROJECT_ID}/survey-app:${BUILD_NUMBER}
                    '''
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
                        sed -i "s|image:.*|image: gcr.io/${PROJECT_ID}/survey-app:${BUILD_NUMBER}|g" deployment.yaml

                        echo "Applying Kubernetes manifests..."
                        kubectl apply -f deployment.yaml
                        kubectl apply -f service.yaml
                    '''
                }
            }
        }
    }
}
```

# 6. Deploy to Kubernetes

Create Kubernetes manifest files:

**deployment.yaml**:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: survey-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: survey-app
  template:
    metadata:
      labels:
        app: survey-app
    spec:
      containers:
        - name: survey-app
          image: gcr.io/your-gcp-project-id/survey-app:${BUILD_NUMBER}
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
          resources:
            requests:
              memory: "512Mi"
              cpu: "250m"
            limits:
              memory: "1Gi"
              cpu: "500m"
```

**service.yaml**:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: survey-app-service
spec:
  selector:
    app: survey-app
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer

```

# Running the Application

After deployment, the application will be accessible via the external IP provided by the Kubernetes service.

- Get the External IP:

```
kubectl get service survey-app-service 
```

- Access the Application:

```
http://EXTERNAL_IP/
```

# Testing with Postman
Use Postman to interact with the RESTful API.

- Endpoints:

  - Create Survey (POST):

```
POST http://EXTERNAL_IP/api/surveys
```

- Body (JSON):


```{
"firstName": "John",
"lastName": "Doe",
"address": "123 Main St",
"city": "Anytown",
"state": "VA",
"zip": "12345",
"telephone": "555-1234",
"email": "john.doe@example.com",
"dateOfSurvey": "2023-10-01",
"likedMost": ["students", "location"],
"interestSource": ["friends", "internet"],
"recommendation": "Very Likely"
}
```

- Get All Surveys (GET):

```
GET http://EXTERNAL_IP/api/surveys
```

- Update Survey (PUT):
```
PUT http://EXTERNAL_IP/api/surveys/{id}
```

- Delete Survey (DELETE):

```
DELETE http://EXTERNAL_IP/api/surveys/{id}
```

# CI/CD Pipeline Details
- Continuous Integration:

  - Code changes are pushed to the repository.
  - Jenkins automatically triggers the pipeline.
  - The application is built using Maven.
  - Docker image is built and pushed to GCR.

- Continuous Deployment:

- The deployment manifests are updated with the new image tag.
- The updated manifests are applied to the GKE cluster.
- The application is deployed or updated in the Kubernetes cluster.


# Deployment on Kubernetes
- High Availability:

  - The deployment is configured with 3 replicas.
  - Kubernetes ensures that the desired number of pods are running.

- Load Balancing:

  - The service type is set to LoadBalancer.
  - Distributes incoming traffic across the pods.

- Scaling:

  - The number of replicas can be adjusted in deployment.yaml.

## References
- Spring Initializr: https://start.spring.io/
- AWS RDS MySQL Setup: Create a MySQL Database
- Docker Documentation: https://docs.docker.com/
- Kubernetes Documentation: https://kubernetes.io/docs/home/
- Jenkins Documentation: https://www.jenkins.io/doc/
- Google Cloud SDK: https://cloud.google.com/sdk/docs/install
-Postman: https://www.postman.com/


