#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Bulk contents file is required";
    exit 1
fi;

FILE=$1

curl -u $ESUSER:$ESPASS -X POST -H 'Content-type: application/x-ndjson' \
     $ESHOST/_bulk \
     --data-binary "@$FILE" | jq '.'
