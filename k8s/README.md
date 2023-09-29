

```
java -jar -DPLANES_NUM_DAYS_DATA=16 -DPLANES_PATH_TO_DATA=/s3/esriplanes/lat88_csv3/ -Dserver.servlet.context-path=/webplanes -Dserver.port=8081 target/webplanes-0.0.1-SNAPSHOT.jar

```

## Create pvc

```
kubectl apply -f webplanes/pvc.yaml
```

PVC uses azurefile-premium to allow RWM access.  I can have many pods attach to this pvc. 

## Load Data 

Create Ubuntu Deployment with pvc mounted

```
kubectl apply -f webplanes/ubuntu-deployment.yaml
```

```
kubectl -n simulators exec -it ubuntu-55df74fb8c-2x2vt -- bash
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
for i in {2..6}; do
  tar xvzf day${i}.tgz
done
```


```
tar xvzf day2.tgz > day2.log 2>&1 &
tar xvzf day3.tgz > day3.log 2>&1 &
```


```
java -jar -DPLANES_NUM_DAYS_DATA=7 -DPLANES_PATH_TO_DATA=/data/lat88_csv3/ -Dserver.port=8080 /opt/webplanes/webplanes-0.0.1-SNAPSHOT.jar
```


## Set DNS 

http://webplanes.westus2.cloudapp.azure.com/

