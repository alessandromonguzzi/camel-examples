# Use Camel-K to exchange files with the SFTP Server

## SFTP Server Setup
```
# create project and load sftp image
oc new-project sftp
oc import-image sftp --from=atmoz/sftp:latest -n openshift --confirm

# create a secret with users.conf 
oc create secret generic sftp-users --from-file=users.conf

#create a configmap with needed files
oc create configmap sftp-ssh --from-file ssh_host_ed25519_key --from-file ssh_host_rsa_key --from-file ssh_host_ed25519_key.pub --from-file ssh_host_rsa_key.pub

#run container as root user (find a better way of running it) so that you can bind port 22
oc adm policy add-scc-to-user anyuid -z default

#start SFTP Server Pod
oc new-app --image-stream=openshift/sftp

#Mount secret
oc set volume deployment/sftp --add --mount-path=/etc/sftp --secret-name=sftp-users --read-only=true

#check that the files are mounted as
# /etc/ssh/ssh_host_ed25519_key
# /etc/ssh/ssh_host_rsa_key
```

## Create Camel-k integration
```
kamel run sftp_example.xml -d camel-quartz -d mvn:org.quartz-scheduler:quartz:2.3.2 -t prometheus.enabled=true
```
## Metrics
Add monitoring capabilities to Camel-K integration

```
#Add monitoring
oc -n openshift-monitoring create configmap cluster-monitoring-config
# set the enableUserWorkload to true
apiVersion: v1
kind: ConfigMap
metadata:
  name: cluster-monitoring-config
  namespace: openshift-monitoring
data:
  config.yaml: |
    enableUserWorkload: true
```
See metrics in OpenShift>Metrics searching for application_camel.*

## Define alert
```
$ cat <<EOF | oc apply -f -
apiVersion: monitoring.coreos.com/v1
kind: PrometheusRule
metadata:
  labels:
    prometheus: example
    role: alert-rules
  name: prometheus-rules
spec:
  groups:
  - name: camel-k.rules
    rules:
    - alert: CamelKAlert
      expr: application_camel_route_processing_seconds > 10
EOF
```
### Utility commands to populate the SFTP Server source directory
Generate a 5GB file and then put it in the source directory
```
mkdir test
cd test
fallocate -l 5GB 5-gb-file
```

### References

[SFTP Server on OpenShift](https://codergists.com/redhat/containers/openshift/camel/2019/10/07/sftp-and-camel-with-ssh-keys-on-openshift.html)
[Introducing Camel-K](https://www.nicolaferraro.me/2018/10/15/introducing-camel-k/)

[Camel K Monitoring](https://camel.apache.org/camel-k/latest/observability/monitoring.html)

