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
        nexusPolicyEvaluation advancedProperties: '', failBuildOnNetworkError: false, iqApplication: selectedApplication('authNservice'), iqScanPatterns: [[scanPattern: '**/target/*.jar']], iqStage: 'build', jobCredentialsId: 'jenkins-nexus'
        archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
      }
      post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
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
                  ], causeString: 'Triggered on $ref', token: 'authNservice', printContributedVariables: true, printPostContent: true, silentResponse: false)
    }
  }
