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

        stage('Generate TestNG Report') {
            steps {
                echo 'Generating TestNG Report'
                junit 'test-output/testng-results.xml' // Using JUnit to publish TestNG results
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
                    serverId: '029272@artifactory',
                    releaseRepo: 'ruchita.nagp.2024',
                    snapshotRepo: 'ruchita.nagp.2024'
                )

                rtMavenRun(
                    pom: 'pom.xml',
                    goals: 'clean install',
                    deployerId: 'deployer'
                )

                rtPublishBuildInfo(serverId: '029272@artifactory')
            }
        }
    }

    post {
        always {
            echo 'Cleaning workspace and finishing pipeline'
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully'
        }
        failure {
            echo 'Pipeline failed'
        }
        unstable {
            echo 'Build was unstable (some tests failed or quality gate not met)'
        }
    }
}
