#!/bin/bash

# Fetch, transform and load current day air quality measurements

TEMP_FILE="$(date | sha256sum | cut -d ' ' -f1).ndjson"

./extractor.sc -bulkIndex airquality -bulkType air_measurements  > $TEMP_FILE
./es/bulk.sh $TEMP_FILE
rm $TEMP_FILE
