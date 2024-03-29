# data folder

If you want to build the Dockerfile-day0 you'll need to download the data from s3 before running docker build.

## Data Creation

See Repo README Data Creation section for details on how planes data was created

## Download Day File from s3

This folder contains unziped files s3://esriplanes/lat88_csv/day0.tgz

There are 17 days of data on s3.  You can access using KEY AKIAYTUXJDCLVGDUNEWN.   You'll need to the access_key.

```
aws s3 ls s3://esriplanes/lat88_csv/
```

```
aws s3 cp s3://esriplanes/lat88_csv/day0.tgz day0.tgz
```

```
tar xvxzf day0.tgz ~/github/webplanes/data
```


Each day is about 9.5G.  Including a full data results in an 4G image.


Day Folder Structor
- The day folder (e.g. 0) contains 86 folder 00 to 86
- Each of the folders 00 to 86 contains 1000 files 000 to 999
- Each file contains 1,000 csv lines that represent where the planes was at that second of the day

There are 86400 seconds in the day.

When a request is made the current time is converted to that second of the day 0 to 86399 and that second of data is looked up and returned.


By precreating the data the service can quickly return up to 1000 planes.

We could calculate positions on the fly; however, that would require more cpu/mem of the service to return the calculate the position and return the data.




The git repo is configured to not include this data.  Each day includes 86400 files one for each second of the day.


The day was created using https://github.com/david618/planes

The resulting docker image was about 10G in size (compressed to 4G)

## About 1 hour loop of data

Deleted folder 04 to 86

Created Symbolic Links

Using a script like

```
#!/usr/bin/env bash

CNT=$1
echo ${CNT}

echo "ln -s 00 ${CNT}"
ln -s 00 ${CNT}
((CNT++))
echo "ln -s 01 ${CNT}"
ln -s 01 ${CNT}
((CNT++))
echo "ln -s 02 ${CNT}"
ln -s 02 ${CNT}
((CNT++))
echo "ln -s 03 ${CNT}"
ln -s 03 ${CNT}
```

Created folder 04 to 86 as symbolic links to day 00 to 03 over and over.

In this case the data will loop every 4000 seconds (about 67 minutes)

The 86 folder shows 1000 files only the first 400 will be used.

The last loop of the day will be 40 minutes.

```
ls -ld *
```

The load time on Kubernetes is about 20 seconds for the 1G image compared to 4 minutes for 10G image.

While the data loops it still should be sufficient for many tests.

