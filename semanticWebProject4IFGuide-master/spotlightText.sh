#!/bin/bash

: ${TEXT?"Please precise TEXT by adding TEXT=your_text_to_annotate"}

OUTPUT_FORMAT=${OUTPUT_FORMAT:-json}
CONFIDENCE=${CONFIDENCE:-0.35}
SUPPORT=${SUPPORT:-0}

curl http://spotlight.sztaki.hu:2222/rest/annotate \
  --data-urlencode "text=$TEXT" \
  --data "confidence=$CONFIDENCE&support=$SUPPORT" \
  -H "Accept: application/$OUTPUT_FORMAT" 2>/dev/null | jq -r '.["Resources"][]["@URI"]'
