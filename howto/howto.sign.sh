#!/bin/sh
#
# WARNING use zipalign BEFORE apksigner

../../../build-tools/29.0.3/zipalign 4  bin/Proj4-release-unsigned.apk  bin/Proj4-release-aligned.apk

../../../build-tools/29.0.3/apksigner sign --out Proj4-release-keysigned.apk -ks ../../../keystore --ks-key-alias TopoDroid bin/Proj4-release-aligned.apk
# Keystore password for signer #1: 

../../../build-tools/29.0.3/apksigner verify --verbose Proj4-release-keysigned.apk
# Verifies
# Verified using v1 scheme (JAR signing): true
# Verified using v2 scheme (APK Signature Scheme v2): true
# Verified using v3 scheme (APK Signature Scheme v3): true
# Number of signers: 1

