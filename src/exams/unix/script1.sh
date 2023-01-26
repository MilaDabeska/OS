#!/bin/bash

path1=$1
path2=$2

if [ "$#" != 2 ]
then
        echo "Potrebni se 2 argumenti"
exit 0
fi

if [ ! -f $path1 ]
then
        echo "Ne postoi input fajlot"
fi

if [ -f $path2 ]
then
        rm $path2
        touch $path2
fi

filepath=`grep filepath input.json | awk '{print $2}' | sed 's/"//g' | sed 's/,//g'`
duration=`grep duration input.json | awk '{print $2}' | sed 's/,//g'`
filesize=`grep filesize input.json | awk '{print $2}' | sed 's/"//g' | sed 's/MB//g'`

srednoVreme=0

br=0
files=
durs=
sizes=

for file in $filepath
do
        files[$br]=$file
        br=`expr $br + 1`
done
br=0

for dur in $duration
do
        durs[$br]=$dur
        br=`expr $br + 1`
done

br=0
for sz in $filesize
do
        srednoVreme=`expr $srednoVreme + $sz`
        sizes[$br]=$sz
        br=`expr $br + 1`
done
br=0

srednoVreme=`expr $srednoVreme / 5`

for i in 0 1 2 3 4
do
        br=`expr $br + 1`
        if [ ${sizes[$i]} -le $srednoVreme ]
        then

                echo ""$br","${files[$i]}","${durs[$i]}",0" >> $2
        else
                echo ""$br","${files[$i]}","${durs[$i]}",1" >> $2
        fi
done

cat $2


