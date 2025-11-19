FROM --platform=$BUILDPLATFORM quay.io/projectquay/golang:1.25 AS builder

WORKDIR /go/src/app
COPY . .

ARG TARGETOS
ARG TARGETARCH
ARG VERSION=dev

RUN make build TARGETOS=$TARGETOS TARGETARCH=$TARGETARCH


FROM scratch
WORKDIR /
COPY --from=builder /go/src/app/kbot .
COPY --from=alpine:latest /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/
ENTRYPOINT ["./kbot"]