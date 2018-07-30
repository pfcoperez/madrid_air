#!/bin/bash

while read jsonEntry; do
    curl -u $ESUSER:$ESPASS -X POST -H 'Content-type: application/json' \
         $ESHOST/airquality/air_measurements \
         -d "$jsonEntry" | jq '.'
done;
