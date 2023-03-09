#!/bin/groovy

@Library('isaac-security-pipeline-utils') _

pipeline {
  agent {
    node {
      label 'macos'
    }
  }

  tools {
    jdk 'JDK 11.0'
  }

  options {
    buildDiscarder(logRotator(artifactDaysToKeepStr: '30', artifactNumToKeepStr: '5', daysToKeepStr: '150', numToKeepStr: '5'))
    disableConcurrentBuilds()
    timeout(time: 15, unit: 'MINUTES')
  }

  triggers {
    cron ('H 3 * * *')
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Android lint') {
      steps {
        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
          sh './gradlew lint'

          script {
            def reportContents = readFile "./ingenicoconnect-sdk/build/reports/lint-results-debug.html"
            // if file does not contain 'No Issues Found', it means linting errors were found
            if (!reportContents.contains("No Issues Found")) {
                error("Android lint warnings found.")
            }
          }
        }
      }
      post {
        always {
            publishHTML(
              [
                 reportName: 'Android Lint Report',
                 reportDir: './ingenicoconnect-sdk/build/reports/',
                 reportFiles: 'lint-results-debug.html',
                 reportTitles: 'Android_Lint_Report',
                 allowMissing: false,
                 keepAll: false,
                 alwaysLinkToLastBuild: false
              ]
            )
        }
      }
    }

    stage('Detekt code analysis') {
      steps {
        script {
          catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
            sh 'detekt -i ingenicoconnect-sdk/src -r html:ingenicoconnect-sdk/build/reports/detekt-report.html -c ingenicoconnect-sdk/detekt-config.yml --build-upon-default-config'
          }
        }
      }
      post {
        always {
            publishHTML(
              [
                reportName: 'Detekt Report',
                reportDir: './ingenicoconnect-sdk/build/reports/',
                reportFiles: 'detekt-report.html',
                reportTitles: 'Detekt_Report',
                allowMissing: false,
                keepAll: false,
                alwaysLinkToLastBuild: false
              ]
          )
        }
      }
    }

    stage('Build repository') {
      steps {
	    script {
	      sh './gradlew :ingenicoconnect-sdk:clean'
	      sh './gradlew :ingenicoconnect-sdk:build -PskipReleaseSigning="true"'
        }
      }
    }

    stage('Run tests & publish test results') {
      steps {
        script {
          sh './gradlew :ingenicoconnect-sdk:testDebugUnitTest'

          junit testResults: 'ingenicoconnect-sdk/build/test-results/testDebugUnitTest/*.xml', allowEmptyResults: false, testDataPublishers: [[$class: 'AttachmentPublisher']]
        }
      }
    }

// Will be fixed for ticket ICON-32435
// stage('OWASP dependency check') {  
//   steps {
//     script {
//       dependencyCheck additionalArguments: '--scan "**/*.apk" --scan "**/*.jar" --scan "**/*.zip" --format XML --format HTML --noupdate',
//                       odcInstallation: 'dependency-check'

//       dependencyCheckPublisher canComputeNew: false

//       archiveArtifacts allowEmptyArchive: true,
//       artifacts: '**/dependency-check-report.*',
//       onlyIfSuccessful: true

//       if (['master', 'develop'].contains(BRANCH_NAME)) {
//         defectDojoPublishReport(product_name: 'Ingenico Connect connect-sdk-client-android',
//                                 scan_type:"Dependency Check Scan")
//       }
//     }
//     recordIssues(tools: [taskScanner(highTags: 'FIXME, HACK', normalTags: 'XXX', lowTags: 'TODO', ignoreCase: true)])
//   }
// }
}

  post {
    failure {
      emailext (
        subject: "${currentBuild.currentResult}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        body: '${JELLY_SCRIPT, template="html"}',
	      to: 'leon.stemerdink@iodigital.com, esmee.kluijtmans@iodigital.com',
        recipientProviders: [developers(), culprits(), requestor()]
      )
    }
  }
}
