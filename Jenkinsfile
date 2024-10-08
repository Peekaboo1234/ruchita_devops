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

        stage('Build') {
            steps {
                echo 'Compiling the code'
                bat 'mvn clean'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo 'Running Unit Tests'
                bat 'mvn test'
            }
        }

        stage('Generate TestNG Report') {
            steps {
                echo 'Generating TestNG Report'
                bat 'mvn surefire-report:report'
                
                // Publish TestNG results
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('Test_SonarQube') {
                    echo 'Running SonarQube Analysis'
                    bat 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar'
                }
            }
        }

        stage('Publish to Artifactory') {
            steps {
                echo 'Publishing to Artifactory'
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

        stage('Archive Artifacts') {
            steps {
                echo 'Archiving artifacts'
                archiveArtifacts artifacts: '**/target/*.jar, **/target/*.pom', fingerprint: true
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
