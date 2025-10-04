# Grafana Dashboard PoC

This folder contains a proof-of-concept setup for managing Grafana dashboards as code.

## Contents

- `provisioning/dashboard-provider.yaml` &mdash; Provisioning manifest that instructs Grafana to load dashboards from a mounted directory.
- `dashboards/sample-service-overview.json` &mdash; A minimal dashboard JSON that surfaces example HTTP metrics.

## Usage

1. Mount the `dashboards/` directory into the Grafana container at `/var/lib/grafana/dashboards/sample-service`.
2. Mount the `provisioning/` directory at `/etc/grafana/provisioning/dashboards/`.
3. Restart Grafana. The dashboard will appear under the **Sample Service** folder.

This layout can be committed to source control and synced to Grafana through your GitOps pipeline.
