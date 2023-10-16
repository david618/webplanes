## webplanes
Web Planes

### Build 

To build at command line  us build.sh.  This defines the required Environment Variables before running mvn install. 

### Copy From S3

mkdir s3
cd s3
aws s3 cp s3://esriplanes/lat88_csv/day0.tgz day0.tgz

export PLANES_PATH_TO_DATA=/Users/davi5017/s3
export PLANES_NUM_DAYS_DATA=1

### Install Java 17

```
brew install openjdk@17
```


### Running  


```
java -jar -DPLANES_NUM_DAYS_DATA=1 -DPLANES_PATH_TO_DATA=/Users/davi5017/s3/ target/webplanes-0.0.1-SNAPSHOT.jar
```

The folder ``/s3/esriplanes/lat88_csv2/`` needs to contain day folders (e.g. 0, 1, 2, ...) 

Each day folder should have group folder 00 to 86.  These are the first two digits of the second into the day. 

Each group folders will have data files 000 to 999.  The files contain the data to return for that second. 

For example each file contains 1,000 csv lines.

```
head 000
0,1596464014600,209.63,16018.98,127.36,1,"Mulia Airport","Copacabana Airport",-1,140.0349,-5.27901
1,1596464014600,364.85,5446.02,-24.5,2,"Owando Airport","Mackenzie Airport",-1,-27.79309,56.49775
2,1596464014600,186.59,4364.23,141.98,3,"Valkenburg Naval Air Base","Naivasha Airport",-1,19.44403,35.10549
3,1596464014600,172.7,10596.5,-100.64,4,"Maewo-Naone Airport","Kaduna Airport",-1,99.87897,-15.65048
4,1596464014600,234.14,4695.05,-119.14,5,"Ta’if Regional Airport","João Simões Lopes Neto International Airport",-1,-11.15546,-12.74674
5,1596464014600,223.53,8956.16,75.78,6,"La Nubia Airport","Geneina Airport",-1,-59.82331,8.71662
6,1596464014600,280.96,902.93,-50.48,7,"Leer-Papenburg Airport","Umiujaq Airport",-1,-62.72487,60.28941
7,1596464014600,279.36,3568.28,49.67,8,"Okadama Airport","Perryville Airport",-1,149.51136,47.50175
8,1596464014600,247.26,2138.39,30.8,9,"Tainan Airport","Scammon Bay Airport",-1,158.70374,55.69375
9,1596464014600,267.47,4840.25,107.04,10,"Corvallis Municipal Airport","Francisco de Miranda Airport",-1,-104.3388,38.30225
```

The data was created using https://github.com/david618/planes



```
java -cp target/planes.jar org.jennings.planes.CreatePlaneEventsFiles routes10000_4days.json 1000 /s3/esriplanes/lat88_csv2 planes now 1 1500000 10000000 txt 88 &
```

The createPlaneEventsFiles create files with 1,000,000 line each.  

The splitPlanesFiles.py script was then used to break the file into day / group folders / files. 

This approach allows this application to quickly return data with very little cpu/mem overhead. 


### Running from Docker

```
docker run -it --rm -v ${HOME}/s3:/data --env=PLANES_NUM_DAYS_DATA=1 --env=PLANES_PATH_TO_DATA=/data/ david62243/webplanes:v2.0
```

```
docker network ls
```

```
docker ps
```

```
docker inspect -f \
'{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' \
e721592417c2
```


```
docker run -it --rm -v ${HOME}/s3:/data --env=PLANES_NUM_DAYS_DATA=1 --env=PLANES_PATH_TO_DATA=/data/ -p 8080:8080 --expose 8080 david62243/webplanes:v2.0
```

Then I was able to access from browser on localhost:8080

