#!/bin/bash

if [ ! $# -eg 1 ]
then
    echo "Invalid script usage: $0 arg1"
    exit 1
fi

files="ls"
for f in $files
do
    if [ -f $f ]
    then
        ext=`echo $f | awk -F.'{ print $2 }'`
        if [ ! -d "$1/$ext" ]
        then
            mkdir -p "$1/$ext"
        fi
        cp $f "$1/$ext/"
    fi
done