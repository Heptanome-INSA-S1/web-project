#!/bin/bash

QUERY=${QUERY:-google} 
OFFSET=${OFFSET:-0} 

curl -A "Mozilla/4.0" "https://www.google.fr/search?q=$QUERY&start=$OFFSET" 2>/dev/null | \
xmllint --html --xpath "//body//p//a/@href" - 2>/dev/null | \
sed -e 's| \(href="[^"]*"\)|\1\n|g' | \
grep 'href="/url?q=' | \
sed -e 's|&amp;[^"]*"|"|g' -e 's|href="/url?q=\([^"]*\)"|\1|g'


