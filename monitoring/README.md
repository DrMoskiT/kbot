# Kubernetes Monitoring Stack (Dev Environment)

This repository contains a monitoring stack deployed in a **Kubernetes dev environment** using **k3d**.  
The stack collects **logs from all cluster nodes and application pods** and visualizes them in Grafana.

## Stack Components

- **Grafana** – visualization and dashboards  
- **Grafana Loki** – log storage and querying  
- **Fluent Bit** – log collection from Kubernetes nodes and pods  
- **Prometheus** – metrics backend (installed via kube-prometheus-stack)

---

## Prerequisites

- Docker
- kubectl
- Helm
- k3d
- GitHub Codespaces or local environment

---

## Cluster Setup

Create a local Kubernetes cluster using k3d:

k3d cluster create demo --agents 3

# Install Monitoring Stack

## Add Helm repositories
helm repo add grafana https://grafana.github.io/helm-charts
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add fluent https://fluent.github.io/helm-charts
helm repo update

## Install Loki
helm upgrade --install loki grafana/loki \
  -n monitoring \
  -f values-loki-dev.yaml

## Install Grafana
helm upgrade --install grafana grafana/grafana \
  -n monitoring \
  -f values-grafana.yaml

## Install Fluent Bit (DaemonSet)
 helm upgrade --install fluent-bit fluent/fluent-bit \
  -n monitoring \
  -f values-fluentbit-k3d.yaml
## Demo Application (Log Source)
kubectl create namespace demo

kubectl -n demo create deployment demo-logger \
  --image=busybox \
  -- /bin/sh -c 'while true; do echo "hello from demo-logger $(date)"; sleep 2; done'

## Grafana Access
kubectl -n monitoring port-forward svc/grafana 3000:80

http://localhost:3000
Default credentials:

login: admin

password: admin

## Loki Data Source
Grafana Loki is configured with the following URL:
http://loki:3100

<img width="1904" height="909" alt="image" src="https://github.com/user-attachments/assets/80952442-10ca-4c8f-9b34-07b788c6ac00" />
