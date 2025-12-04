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
                echo "🔍 Running linter..."
                // якщо нема make lint — можна замінити на go vet ./...
                sh '''
                    if make help | grep -q "lint"; then
                      make lint
                    else
                      echo "No make lint, running go vet ./..."
                      go vet ./...
                    fi
                '''
            }
        }

        stage('Tests') {
            when {
                expression { !params.SKIP_TESTS }
            }
            steps {
                echo "🧪 Running tests..."
                sh '''
                    if make help | grep -q "test"; then
                      make test
                    else
                      echo "No make test, running go test ./..."
                      go test ./...
                    fi
                '''
            }
        }

        stage('Build') {
            steps {
                echo "🔨 Building for ${params.OS}/${params.ARCH}"

                sh """
                    mkdir -p build
                    GOOS=${params.OS} \
                    GOARCH=${params.ARCH} \
                    CGO_ENABLED=0 \
                    go build -o build/kbot-${params.OS}-${params.ARCH} .
                """

                echo "Binary created: build/kbot-${params.OS}-${params.ARCH}"
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
