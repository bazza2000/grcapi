pipeline {
  agent any
  stages {
    stage('Build') {
      agent {
        docker {
          image 'maven:3.6.1-jdk-8-alpine'
          args '-v /root/.m2:/root/.m2 -v /root/artifacts:/artifacts --network root_default'
        }

      }
      steps {
        sh 'mvn -B clean package sonar:sonar -Dsonar.host.url=http://sonarqube:9000'
        sh 'cp -rp target /artifacts'
        //nexusPolicyEvaluation advancedProperties: '', failBuildOnNetworkError: false, iqApplication: selectedApplication('authNservice'), iqScanPatterns: [[scanPattern: '**/target/*.jar']], iqStage: 'build', jobCredentialsId: 'jenkins-nexus'
        archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
      }
    }
    stage('Containerize') {
      steps {
        sh 'cp -rp /artifacts/target .'
        sh "/usr/bin/docker build -t  ${env.SERVICE_URL}:${env.SERVICE_PORT}/${env.APP_NAME}:${env.BUILD_ID} . "
        sh "/usr/bin/docker login  ${env.SERVICE_URL}:${env.SERVICE_PORT} -u ${GITHUB_ASH_CREDS_USR} -p ${GITHUB_ASH_CREDS_PSW}"
        sh "/usr/bin/docker push   ${env.SERVICE_URL}:${env.SERVICE_PORT}/${env.APP_NAME}:${BUILD_NUMBER}"
        sh "/usr/bin/docker tag   ${env.SERVICE_URL}:${env.SERVICE_PORT}/${env.APP_NAME}:${BUILD_NUMBER} ${env.SERVICE_URL}:${env.SERVICE_PORT}/${env.APP_NAME}:latest"
        sh "/usr/bin/docker push   ${env.SERVICE_URL}:${env.SERVICE_PORT}/${env.APP_NAME}:latest"
      }
    }
  }
  environment {
    SERVICE_URL = 'docker.viosystems.com'
    SERVICE_PORT = '8443'
    APP_NAME = 'grcapi'
    GITHUB_ASH_CREDS = credentials('jenkins-user-for-nexus-repository')
  }
  options {
    timeout(time: 1, unit: 'HOURS')
    disableConcurrentBuilds()
    buildDiscarder(logRotator(daysToKeepStr: '30', numToKeepStr: '10', artifactDaysToKeepStr: '30', artifactNumToKeepStr: '10'))
    timestamps()
  }
  triggers {
    GenericTrigger(genericVariables: [
                    [key: 'ref', value: '$.ref']
                  ], causeString: 'Triggered on $ref', token: 'grcapi', printContributedVariables: true, printPostContent: true, silentResponse: false)
    }
  }
