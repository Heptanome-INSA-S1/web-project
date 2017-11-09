#!/bin/bash

: ${URI?"Please precise URI by adding URI=your_URI"}
OUTPUT_FORMAT=${OUTPUT_FORMAT:-rdf+xml}

curl -L -H "Accept: application/$OUTPUT_FORMAT" $URI
