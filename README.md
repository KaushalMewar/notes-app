# Note-Taking Application 📓
Welcome to the Note-Taking Application repository! This project demonstrates how to build a backend for a note-taking application using Spring Boot and MongoDB. The app allows users to record notes and is designed to be scalable and cloud-ready. Currently, the API supports creating, retrieving, updating, and deleting notes.

# Overview 🌟
This project provides a RESTful API for a note-taking application. This project demonstrates how to build, deploy, and scale a note-taking application using Kubernetes and Docker. It includes detailed instructions on setting up a local development environment with Minikube and handling various aspects of Kubernetes, including scaling and resource limits.

# Features 🚀
- Create Notes: Add new text notes to the database.
- Retrieve Notes: View all notes or retrieve a specific note by ID.
- Update Notes: Edit existing notes.
- Delete Notes: Remove notes from the database.
- Cloud-Ready Deployment: Designed for easy deployment to both local and cloud Kubernetes clusters.
- Scalable Architecture: Built to scale with Kubernetes, ensuring high availability and fault tolerance.

# Getting Started 🏗️
Follow these steps to get your application up and running:

#### Prerequisites ⚙️
- Java Development Kit (JDK): Ensure JDK 11 or later is installed.
- Maven: For building the project.
- Docker: To containerize the application.
- Kubernetes (Minikube or AWS EKS): For deploying the application.
- MongoDB: The database for storing notes.

#### Setup 🛠️
- Clone the Repository

##### Create a docker network
```Docker
docker network create notes-app-network
```

##### Run MongoDB docker image
```Docker
docker run -d --name=mongodb --rm --network=notes-app-network mongodb/mongodb-community-server:latest
```

##### Get the IP of the MongoDB docker container and set an environment variable
```Docker
docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mongodb
```
```bash
export MONGO_URL=mongodb://172.X.X.X:27017/notes
```

##### Build notes-api app
```Maven
mvn clean install
```

##### Build notes-api docker image
```Docker
docker build -t notes-api:latest .
```
##### Tag the image to push
```bash
docker tag notes-api <dockerhub-user-id>/notes-api:1.0.0
```

##### Push docker image
```Docker
docker push <dockerhub-user-id>/notes-api:1.0.0
```

##### Run notes-app (local)
```Docker
docker run -d --name=notes-api --rm --network=notes-app-network -p 8080:8080 -e MONGO_URL=mongodb://172.X.X.X:27017/notes notes-api:latest
```

* docker run: Starts a new Docker container.
* -d: Runs the container in detached mode (in the background).
* --name=notes-api: Assigns a name (notes-api) to the container.
* --rm: Automatically removes the container when it stops.
* --network=notes-app-network: Connects the container to the specified network (notes-app-network).
* -p 8080:8080: Maps port 8080 of the host to port 8080 of the container.
* -e MONGO_URL=mongodb://mongo:27017/notes: Sets an environment variable MONGO_URL inside the container.
* notes-api:latest: Specifies the image to use (notes-api with the latest tag).

##### notes-api docker image (dockerhub)
```bash
docker pull kaushalaltair/notes-api:1.0.0
```
# Deploying with Kubernetes 🌐
### Using Minikube 🖥️

1. Start Minikube:
```bash
minikube start
```
2. Create Kubernetes Resources:
```bash
kubectl apply -f kube
```
3. Verify Deployment:
```bash
kubectl get pods --watch
```
4. Get URL
```bash
minikube service notes-api --url
```

# Scaling and Resource Limits 📈
1. Scale Application:
```bash
kubectl scale --replicas=10 deployment/notes-api
```
2. Check Resource Limits:
- Current Pods:
```bash
kubectl get pods --all-namespaces
```
- Running and Pending Pods:
```bash
kubectl get pods -l app=notes-api --field-selector='status.phase=Running' --no-headers | wc -l
kubectl get pods -l app=notes-api --field-selector='status.phase=Pending' --no-headers | wc -l
```
- Scale Down if Necessary:
```bash
kubectl scale --replicas=5 deployment/notes-api
```
# Cleaning Up 🧹
- Stop Minikube:
```bash
minikube stop
```
```bash
minikube delete --all
```

# Future work : Attaching Images 📸
The next phase involves expanding the application's capabilities to handle image attachments and associate them with notes, which will enhance its functionality.

# Conclusion 🎉
This project demonstrates the power of cloud-native development and Kubernetes for scalable application management. 


