#!/bin/bash

: ${URL?"Please precise URL by adding URL=your_url"}

curl -A "Mozilla/4.0" $URL 2>/dev/null | xmllint --html --xpath "//body//p" - 2>/dev/null | sed -e 's|<[^>]*>||g'

