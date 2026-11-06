#!/bin/sh
#
cd bin
if [ "$1" = "debug" ]; then 
  zipfile="Proj4-$1-unaligned.apk"
else 
  zipfile="Proj4-release-unsigned.apk"
fi

echo "Multidex try to add classes2.dex to $zipfile"
if [ -f classes2.dex ]; then
  zip -u $zipfile classes2.dex
fi
cd -

