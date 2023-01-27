#!/bin/bash

calcSize(){

  total=0

  files=`ls $1`

  for f in $files
  do
    if [ -f "$1/$f" ]
    then
      size=`ls -l "$1/$f" | awk '{ print $5 }'`
      total=`expr $total + $size`
    else
      dirSize=`calcSize "$1/$f"`
      total=`expr $total + $dirSize`
    fi
  done
  echo $total
}
calcSize $1