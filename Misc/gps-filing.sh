#!/bin/bash

echo filing the gps files!

year=`date -d yesterday +%Y`
doy=`date -d yesterday +%j`

#debug:
echo $year
echo $doy

dire="TAC${year}"
#debug:
echo $dire

if [[ ! -e "$dire" ]]
	then mkdir $dire
	#deug:
	echo made directory
fi

for X in "A" "T"
do
	oldFileName="NASA_${year}_${doy}${X}_PORT_01.csv"
	newFileName="NY${doy}${X}.csv"
	cp $oldFileName ./$dire/$newFileName
	#note: change cp to mv
done

#edge case:
oldFileName="NASA_${year}_${doy}C.txt" 
newFileName="NY${doy}C.txt"

cp $oldFileName ./$dire/$newFileName

#debug:
echo done ?


