# ===== Образ для GHCR =====
IMAGE_REGISTRY ?= ghcr.io
IMAGE_OWNER    ?= drmoskit
IMAGE_NAME     ?= kbot

APP            := $(IMAGE_NAME)

VERSION        := $(shell git describe --tags --abbrev=0)-$(shell git rev-parse --short HEAD)
TARGETOS       ?= linux
TARGETARCH     ?= amd64
PLATFORM       := $(TARGETOS)/$(TARGETARCH)
BUILD_DIR      := build

# Повний тег образу
IMAGE := $(IMAGE_REGISTRY)/$(IMAGE_OWNER)/$(IMAGE_NAME):$(VERSION)-$(TARGETOS)-$(TARGETARCH)
TEST_IMAGE := $(IMAGE_REGISTRY)/$(IMAGE_OWNER)/$(IMAGE_NAME):$(VERSION)-test-$(TARGETOS)-$(TARGETARCH)

format:
	gofmt -s -w ./

lint:
	golint

test:
	go test -v

get:
	go get

build: format
	CGO_ENABLED=0 GOOS=${TARGETOS} GOARCH=${TARGETARCH} go build -v -o kbot -ldflags "-X 'github.com/drmoskit/kbot/cmd.appVersion=${VERSION}'"

linux:
	$(MAKE) build TARGETOS=linux TARGETARCH=amd64
	mkdir -p $(BUILD_DIR)
	mv kbot $(BUILD_DIR)/$(APP)-linux-amd64

linux-arm:
	$(MAKE) build TARGETOS=linux TARGETARCH=arm64
	mkdir -p $(BUILD_DIR)
	mv kbot $(BUILD_DIR)/$(APP)-linux-arm64

macos:
	$(MAKE) build TARGETOS=darwin TARGETARCH=amd64
	mkdir -p $(BUILD_DIR)
	mv kbot $(BUILD_DIR)/$(APP)-darwin-amd64

macos-arm:
	$(MAKE) build TARGETOS=darwin TARGETARCH=arm64
	mkdir -p $(BUILD_DIR)
	mv kbot $(BUILD_DIR)/$(APP)-darwin-arm64

windows:
	$(MAKE) build TARGETOS=windows TARGETARCH=amd64
	mkdir -п $(BUILD_DIR)
	mv kbot $(BUILD_DIR)/$(APP)-windows-amd64

image:
	docker buildx build \
	  --platform=$(PLATFORM) \
	  --build-arg TARGETOS=$(TARGETOS) \
	  --build-arg TARGETARCH=$(TARGETARCH) \
	  --build-arg VERSION=$(VERSION) \
	  -t $(IMAGE) \
	  --load .

push:
	docker push $(IMAGE)

test-image:
	docker buildx build \
	  --platform=$(PLATFORM) \
	  --build-arg TARGETOS=$(TARGETOS) \
	  --build-arg TARGETARCH=$(TARGETARCH) \
	  --build-arg VERSION=$(VERSION) \
	  -f Dockerfile.test \
	  -t $(TEST_IMAGE) \
	  --load .

clean:
	rm -rf $(BUILD_DIR) kbot
	- docker rmi $(IMAGE) || true
	- docker rmi $(TEST_IMAGE) || true

