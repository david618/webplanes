import sys
import os
import glob

# Create planes files using
# java -cp target/planes.jar org.jennings.planes.CreatePlaneEventsFiles routes10000_4days.json 1000 /opt/kafka/azureuser/lat88_csv planes now 1 500000 10000000 txt 88
# Create tgz  tar cvzf day2.tgz 2
# filename=day0.tgz
# Copied to AWS S3: aws s3 cp $filename s3://esriplanes/lat88_csv/$filename
# Copy from AWS S3: aws s3 cp s3://esriplanes/lat88_csv/$filename $filename 
# Folder/File structure 
#    Folder for day 0, 1, 2 
#    One file for each second of day Files grouped by 1000 
#    Folder folder 00 to 85 has files 000 to 999
#    Folder 86 has files 000 to 399  
#    Each files has about 1,000 planes messages; any message with lat outside of [-88,88] was removed 
#    Latitudes outside of [-88,88] do not work in default ArcGIS only Projection (Web Mercator) 




dayNumber = 0
folderNumber = 0
fileNumber = 0

dayName = '%01d' % dayNumber

folderName = os.path.join(dayName, '%02d' % folderNumber)
if not os.path.isdir(folderName):
    os.makedirs(folderName)

fileName = '%03d' % fileNumber
filename = os.path.join(folderName, fileName)

fout=open(filename, "w")

files=list(glob.glob('planes?????'))
files.sort()
for file in files:
    print file
    fin=open(file)
    previousPlaneID=-1
    for line in fin:
        parts=line.split(",")
        planeID=int(parts[0])
        if planeID < previousPlaneID:
            fout.close()
            fileNumber += 1
            if folderNumber == 86 and fileNumber == 400:
                # New Day
                fileNumber = 0
                folderNumber = 0
                dayNumber += 1
                dayName = '%01d' % dayNumber
                folderName = os.path.join(dayName, '%02d' % folderNumber)
                if not os.path.isdir(folderName):
                    os.makedirs(folderName)

            elif fileNumber == 1000:
                fileNumber = 0
                folderNumber += 1
                folderName = os.path.join(dayName, '%02d' % folderNumber)
                if not os.path.isdir(folderName):
                    os.makedirs(folderName)
            fileName = '%03d' % fileNumber
            filename = os.path.join(folderName, fileName)
            fout=open(filename, "w")

        parts[1]="REPL_TS"
        line = ",".join(parts)
        fout.write(line)
        previousPlaneID=planeID