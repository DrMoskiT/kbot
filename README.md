# kbot

A Telegram bot

## Features

- Simple and intuitive command interface
- Cross-platform support (Linux, Darwin, Windows)
- Multi-architecture support (amd64, arm64)

## Prerequisites

- Go 1.16 or later
- Telegram Bot Token (set as TELE_TOKEN environment variable)
- Required Go packages:
  - github.com/spf13/cobra
  - github.com/stianeikeland/go-rpio
  - gopkg.in/telebot.v4

## Installation

1. Clone the repository:
```bash
git clone https://github.com/kbot/kbot.git
cd kbot
```

2. Set up your Telegram Bot Token:
```bash
export TELE_TOKEN="your_telegram_bot_token"
```

3. Build the application:
```bash
make build
```

## Build Options

The project supports various build targets through Makefile:

- `make format` - Format Go code
- `make lint` - Run golint
- `make test` - Run tests
- `make get` - Get dependencies
- `make build` - Build the application
- `make image` - Build Docker image
- `make push` - Push Docker image to registry
- `make clean` - Clean build artifacts

### Build Configuration

You can customize the build by setting environment variables:
```bash
TARGETOS=linux    # Target OS (linux, darwin, windows)
TARGETARCH=arm64  # Target architecture (amd64, arm64)
```

## Usage

Start the bot:
```bash
./kbot start
```

### Available Commands

- `hello` - Get a greeting from the bot

## Development

The project uses Cobra for CLI command management and go-rpio for GPIO control.

### Project Structure

- `cmd/` - Contains the main command implementations
  - `kbot.go` - Main bot implementation and traffic light control
  - `root.go` - Root command configuration
  - `version.go` - Version command implementation

## Versioning

The application version is automatically generated during build using:
- Latest git tag
- Short commit hash

## License

This project is licensed under the MIT License - see the LICENSE file for details.
