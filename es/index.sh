#!/bin/bash

while read jsonEntry; do
    curl -u $ESUSER:$ESPASS -X POST -H 'Content-type: application/json' \
         $ESHOST/airquality/madrid_air_measurements \
         -d "$jsonEntry" | jq '.'
done;
