pipeline {
    agent any

    parameters {
        choice(
            name: 'OS',
            choices: ['linux', 'darwin', 'windows'],
            description: 'Target operating system'
        )
        choice(
            name: 'ARCH',
            choices: ['amd64', 'arm64'],
            description: 'Target architecture'
        )
        booleanParam(
            name: 'SKIP_TESTS',
            defaultValue: false,
            description: 'Skip running tests'
        )
        booleanParam(
            name: 'SKIP_LINT',
            defaultValue: false,
            description: 'Skip running linter'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/DrMoskiT/kbot.git'
            }
        }

        stage('Show parameters') {
            steps {
                echo "OS=${params.OS}"
                echo "ARCH=${params.ARCH}"
                echo "SKIP_TESTS=${params.SKIP_TESTS}"
                echo "SKIP_LINT=${params.SKIP_LINT}"
            }
        }

        stage('Lint') {
            when {
                expression { !params.SKIP_LINT }
            }
            steps {
                echo "🔍 Simulated linter run (no tools installed in Jenkins image)"
                sh 'echo "lint ok"'
            }
        }

        stage('Tests') {
            when {
                expression { !params.SKIP_TESTS }
            }
            steps {
                echo "🧪 Simulated tests run (no Go installed in Jenkins image)"
                sh 'echo "tests ok"'
            }
        }

        stage('Build') {
            steps {
                echo "🔨 Simulated build for ${params.OS}/${params.ARCH}"

                sh """
                    mkdir -p build
                    echo "Dummy binary for ${params.OS}/${params.ARCH}" > build/kbot-${params.OS}-${params.ARCH}.dummy
                """

                echo "Binary created: build/kbot-${params.OS}-${params.ARCH}.dummy"
            }
        }
    }

    post {
        success {
            echo "✅ Build finished successfully"
            archiveArtifacts artifacts: 'build/**', allowEmptyArchive: true
        }
        failure {
            echo "❌ Build failed"
        }
    }
}
