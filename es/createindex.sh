#!/bin/bash

curl -u "$ESUSER:$ESPASS" -X PUT -H 'Content-type: application/json' \
     "$ESHOST/airquality" \
     -d "@./payloads/index_creation.json"
