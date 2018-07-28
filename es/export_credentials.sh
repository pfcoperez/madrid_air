#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Credentials file file is required";
    exit 1
fi;

FILE=$1

export ESHOST=$(sed -n "1p" < $FILE)
export ESUSER=$(sed -n "2p" < $FILE)
export ESPASS=$(sed -n "3p" < $FILE)
