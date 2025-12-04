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
                // якщо хочеш — можеш використати params.GIT_REPO/BRANCH,
                // для простоти жорстко вказуємо твій репозиторій
                git branch: 'develop', url: 'https://github.com/DrMoskiT/kbot.git'
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

        stage('Build') {
            steps {
                echo "Here you will run make/go build with OS/ARCH"
                // приклад:
                // sh "GOOS=${params.OS} GOARCH=${params.ARCH} go build -o kbot-${params.OS}-${params.ARCH} ."
            }
        }
    }

    post {
        success {
            echo "✅ Build finished successfully"
        }
        failure {
            echo "❌ Build failed"
        }
    }
}
