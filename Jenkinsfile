pipeline { 
    agent any

    tools {
        maven 'Maven 3.9.6'  
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning the repository'
                git branch: 'master', url: 'https://github.com/ruchita10pathak/ruchita_devops.git'
            }
        }

        stage('Code Build') {
            steps {
                echo 'Compiling the code'
                bat 'mvn clean'
            }
        }

        stage('Unit Test') {
            steps {
                echo 'Running Unit Tests'
                bat 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('Test_SonarQube') {
                    bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar'
                }
            }
        }

        stage('Publish to Artifactory') {
            steps {
                rtMavenDeployer(
                    id: 'deployer',
                    serverId: '029272@artifactory', // Artifactory server ID in Jenkins
                    releaseRepo: 'ruchita.nagp.2024', // Release repo
                    snapshotRepo: 'ruchita.nagp.2024'  // Snapshot repo
                )

                rtMavenRun(
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: 'deployer'
                )

                rtPublishBuildInfo(
                    serverId: '029272@artifactory' // Artifactory server ID
                )
            }
        }
    }

    post {
        always {
            echo 'Cleaning workspace and finishing pipeline'
            cleanWs()  // Clean workspace after each run
        }
        success {
            echo 'Pipeline completed successfully'

            // Publish TestNG Results
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/surefire-reports',
                reportFiles: 'emailable-report.html',
                reportName: 'TestNG Report'
            ])
        }
        failure {
            echo 'Pipeline failed'
        }
        unstable {
            echo 'Build was unstable (some tests failed or quality gate not met)'
        }
    }
}
