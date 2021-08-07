pipeline {
  agent {
    kubernetes {
      label 'maven-docker-worker'
      defaultContainer 'jnlp'
      idleMinutes '360'
      instanceCap '5'
      yaml """
apiVersion: v1
kind: Pod
metadata:
  namespace: jenkins
labels:
  component: ci
spec:
  # Use service account that can deploy to all namespaces
  # serviceAccountName: cd-jenkins
  containers:
  - name: maven
    image: maven:3.6.3-jdk-11
    imagePullPolicy: IfNotPresent
    command:
    - cat
    tty: true
    volumeMounts:
      - mountPath: "/root/.m2"
        name: m2
  - name: sonar-scanner
    image: sonarsource/sonar-scanner-cli
    imagePullPolicy: IfNotPresent
    command:
    - cat
    tty: true
  - name: docker
    image: docker:19.03.13
    imagePullPolicy: IfNotPresent
    command:
    - cat
    tty: true
    volumeMounts:
    - mountPath: /var/run/docker.sock
      name: docker-sock
  volumes:
    - name: docker-sock
      hostPath:
        path: /var/run/docker.sock
    - name: m2
      persistentVolumeClaim:
        claimName: m2
---
"""
    }
  }
  stages {
     stage('Test') {
       steps {
         container('maven') {
           sh "mvn test"
         }
       }
     }
    stage('Package') {
      steps {
        container('maven') {
          sh 'mvn package'
        }
      }
    }
//     stage('Verify') {
//       steps {
//         container('maven') {
//           sh 'mvn verify'
//         }
//       }
//     }
    stage('Static Analysis') {
      environment {
        scannerHome = tool 'SonarQube'
      }
      steps {
        container('sonar-scanner') {
          withSonarQubeEnv('SonarQube') {
            sh "${scannerHome}/bin/sonar-scanner"
          }
        }
      }
    }
    stage('Deploy') {
      environment {
        registryDomain = "docker.interswitch.co.ke:30003"
        registry = "https://${registryDomain}"
        registryCredential = 'docker-credentials'
        PATH = "${dockerHome}/bin:${env.PATH}"
        repo = "test"
        project = "identity-service"
        version = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
        fullName = "${registryDomain}/${repo}/${project}"
      }
      steps {
        container('docker') {
          script{
            def defaultLatestImage = docker.build("${fullName}", ".")
            def taggedImage = docker.build("${fullName}:${version}", ".")
            docker.withRegistry(registry, registryCredential) {
              defaultLatestImage.push()
              taggedImage.push()
            }
          }
        }
      }
    }
  }
}