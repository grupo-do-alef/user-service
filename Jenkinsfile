pipeline {
    agent any

    parameters {
        booleanParam(name: 'DEPLOY_TO_AWS', defaultValue: false, description: 'Cria/inicia a EC2 e publica o user-service.')
    }

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
        AWS_PAGER = ''
        AWS_PROFILE = 'lab'
        SOURCE_DIR = '/workspace/user-service'
    }

    stages {
        stage('Validar acesso AWS') {
            when { expression { params.DEPLOY_TO_AWS } }
            steps {
                sh 'aws sts get-caller-identity'
            }
        }

        stage('Compilar e testar') {
            steps {
                sh '/workspace/jenkins/scripts/maven-verify.sh user-service "$SOURCE_DIR"'
            }
        }

        stage('Garantir EC2') {
            when { expression { params.DEPLOY_TO_AWS } }
            steps {
                sh '/workspace/jenkins/scripts/ensure-ec2.sh user-service > .ec2.env'
            }
        }

        stage('Publicar') {
            when { expression { params.DEPLOY_TO_AWS } }
            steps {
                sshagent(credentials: ['ec2-deploy-key']) {
                    sh '. ./.ec2.env && cd "$SOURCE_DIR" && /workspace/jenkins/scripts/deploy-compose.sh user-service "$PUBLIC_IP" "http://localhost:3000"'
                }
            }
        }
    }
}
