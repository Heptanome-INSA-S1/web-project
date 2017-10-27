#!/bin/bash

: ${URL?"Please precise URL by adding URL=your_url"}
: ${APIKEY?"Please precise APIKEY by adding APIKEY=your_alchemy_api_key"}

OUTPUT_FORMAT=${OUTPUT_FORMAT:-json} 
curl "http://access.alchemyapi.com/calls/url/URLGetText?apikey=$APIKEY&url=$URL&outputMode=$OUTPUT_FORMAT"
