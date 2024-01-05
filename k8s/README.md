
# webplanes

## Command used on old server

```
java -jar -DPLANES_NUM_DAYS_DATA=16 -DPLANES_PATH_TO_DATA=/s3/esriplanes/lat88_csv3/ -Dserver.servlet.context-path=/webplanes -Dserver.port=8081 target/webplanes-0.0.1-SNAPSHOT.jar
```


## Build webplanes

```
./build.sh
```

```
docker build -t david62243/webplanes:v2.0 .
docker push david62243/webplanes:v2.0
```

## Set Context

```
export KUBECONFIG=/users/davi5017/simulators-vel2023.kubeconfig
```

## Create namespace

```
NAMESPACE=webplanes
kubectl create ns ${NAMESPACE}
```

## Create pvc

```
kubectl -n ${NAMESPACE} apply -f k8s/pvc.yaml
```

PVC uses azurefile-premium to allow RWM access.  I can have many pods attach to this pvc.

## Load Data

Create Ubuntu Deployment with pvc mounted

```
kubectl -n ${NAMESPACE} apply -f k8s/load-deployment.yaml
```

```
kubectl -n webplanes exec -it webplanes-74bf8b5f45-p55ss -- bash
```

```
apt udpate
apt install awscli
```

```
aws configure
```


Used "AKIAY...UNEWN"

```
aws s3 ls s3://esriplanes
aws s3 ls s3://esriplanes/lat88_csv/
```

```
cd /data
```

```
for i in {0..16}; do
 aws s3 cp s3://esriplanes/lat88_csv/day${i}.tgz .
done
```

```
for a in *.tgz:
  tar xvzf ${a}
done
```

```
tar xvzf day0.tgz > day0.log 2>&1 &
tar xvzf day1.tgz > day1.log 2>&1 &
tar xvzf day2.tgz > day2.log 2>&1 &
tar xvzf day3.tgz > day3.log 2>&1 &
```

Unpacking the gzip is very slow.  The first 20% only took about 30 minutes; but as more data loads the unpacking took longer and longer.  It took over 8 hours to unarchive this code.

I tried azcopy; however, I wasn't able to successfully copy the files.

I'm also not thrilled with the disk i/o for Azure's file.csi.azure.com provisioner.

Thinking we could try k3s and just use an NFS Share and HostPath.  The a standard volume we should be able to mount easily.

### Start webplanes with 4 days of data

```
java -jar -DPLANES_NUM_DAYS_DATA=3 -DPLANES_PATH_TO_DATA=/data/lat88_csv3/ -Dserver.port=8080 /opt/webplanes/webplanes-0.0.1-SNAPSHOT.jar
```


## Set DNS

Found the Public IP in the MC Resource Group and set the DNS name label to webplanes.

http://webplanes.westus2.cloudapp.azure.com/



```
tar xvzf day4.tgz > day4.log 2>&1 &
tar xvzf day5.tgz > day5.log 2>&1 &
tar xvzf day6.tgz > day6.log 2>&1 &
tar xvzf day7.tgz > day7.log 2>&1 &
```


```
tar xvzf day8.tgz > day8.log 2>&1 &
tar xvzf day9.tgz > day9.log 2>&1 &
tar xvzf day10.tgz > day10.log 2>&1 &
tar xvzf day11.tgz > day11.log 2>&1 &
```

```
tar xvzf day12.tgz > day12.log 2>&1 &
tar xvzf day13.tgz > day13.log 2>&1 &
tar xvzf day14.tgz > day15.log 2>&1 &
tar xvzf day15.tgz > day15.log 2>&1 &
tar xvzf day16.tgz > day16.log 2>&1 &
```

The slow unzip is partly because of throttling.

I updated the disk from 512G to 4096G (4T).  Once the data is loaded we can scale the Storage Account back down to 512G.

After loading data I changed size back to 512G.

https://learn.microsoft.com/en-us/troubleshoot/azure/azure-storage/files-troubleshoot-performance?tabs=windows

## velocitysimdata Storage Account

Created a new Storage Account velocitysimdata in a4iotworkspace RG of subscription 844b25fe-7752-4bbd-ba37-ad545aa62be0

Create a webplanes fileshare in the Storage Account.

Used Microsoft Azure Storage Explorer to copy files from the pvc.

Next time instead of downloading from s3 and unzipping; we should be able to just copy from this Storage Account to the Storage account pvc folder.

### Create LB Service (optional)

Makes the service available from http via IP or DNS.

```
kubectl -n ${NAMESPACE} apply -f k8s/service-lb.yaml
```

The creates a LoadBalancer type service. For AKS this creates a Public IP.  You can access with the IP or set DNS name (e.g. http://webplanes.westus2.cloudapp.azure.com).

### Create Service using Ingress Controller

Creates a Public IP and configures it to automatically maintain valid SSL certificate using LetsEncrypt.

#### cert-manager

```
helm repo add jetstack https://charts.jetstack.io
helm repo update
```

```
helm install \
  cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  --create-namespace \
  --version v1.13.1 \
  --set installCRDs=true
```

#### Open Port

To allow LetsEncrypt/Certmanager create a certificate; add port 8089 to the Network Security Group (NSG) in the Managed cluster.

#### Nginx Ingress


https://docs.nginx.com/nginx-ingress-controller/

This places the nginx-controller in the same namespace as webplanes.

```
helm -n webplanes install --set controller.ingressClass.name=webplanes nginx-webplanes oci://ghcr.io/nginxinc/charts/nginx-ingress
```

Find and update the Public IP:

```
kubectl apply -f k8s/issuer.yaml
kubectl apply -f k8s/ingress.yaml
```

#### Allow http Access

The above configuration forces all requests to https and ssl.

The following creates a NodePort service.

```
kubectl apply -f k8s/service-nodeport.yaml
```

Then in the Managed Cluster allow access from anyone on port 30001.

Then add a Load Balancing rule to allow
- Frontend IP for websplanes
- Backend kubernetes (That's the Kubernetes Nodes)
- Configure Port 30002 from Public IP to be be passed to 30001 of the nodes

```
http://webplanes.westus2.cloudapp.azure.com:30002
```

