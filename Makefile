ANT    = /home/programs/apache-ant-1.8.2/bin/ant
CC     = gcc
CPLUS  = g++
AR     = ar
CFLAGS = -g -O0 -Wall -DMAIN

# verbose flag for ANT
AFLAGS = -v 

VERSION = `grep versionName AndroidManifest.xml | sed -e 's/ *android:versionName=//' | sed -e 's/"//g' `
TARGET_SDK = `grep targetSdkVersion AndroidManifest.xml | sed -e 's/ *android:targetSdkVersion=//' | sed -e 's/"//g' `
VERSION_CODE = `grep versionName AndroidManifest.xml | sed -e 's/ *android:versionCode=//' | sed -e 's/"//g' `

# APPCODE must coincide with the main source folder
APPCODE = proj4
# APPNAME is the application name, as in build.xml
APPNAME = Proj4
APPVERSION = $(APPNAME)-$(VERSION)
LOGNAME = proj4
PACKAGE = com.topodroid.$(APPCODE)
VERSION_TARGET = $(VERSION)-$(TARGET_SDK)

default: debug-signed

debug:
	$(ANT) debug

release:
	$(ANT) release
	@mv bin/$(APPNAME)-release.apk $(APPVERSION)-release.apk

signed:
	$(ANT) release
	@echo "Version $(VERSION)"
	./howto/sign.sh
	@mv $(APPNAME)-release-keysigned.apk $(APPVERSION)-$(TARGET_SDK).apk
	@rm Proj4-release-keysigned.apk.idsig

debug-signed:
	$(ANT) debug
	./howto/sign-debug.sh

install-nosigned:
	adb install -r bin/$(APPNAME)-debug.apk

install:
	adb install -r bin/$(APPNAME)-debug-keysigned.apk


rebuild:
	$(ANT) clean
	$(ANT) debug

perms:
	adb shell appops set com.topodroid.proj4 READ_EXTERNAL_STORAGE allow
	adb shell appops set com.topodroid.proj4 WRITE_EXTERNAL_STORAGE allow
	adb shell appops set com.topodroid.proj4 MANAGE_EXTERNAL_STORAGE allow

clean:
	$(ANT) clean

log:
	adb logcat | grep proj4

git-add:
	yes | git add -p

git-pull:
	git pull

SRC = \
  ./AndroidManifest.xml \
  ./ant.properties \
  ./assets \
  ./build.properties \
  ./build.xml \
  ./db \
  ./epsg.txt \
  ./howto/* \
  ./icons/* \
  ./jni \
  ./LICENSE \
  ./local.properties \
  ./Makefile \
  ./proguard-project.txt \
  ./project.properties \
  ./README \
  ./res \
  ./src 

version:
	echo $(VERSION)

archive:
	tar -chzf ../proj4.tgz --exclude-vcs $(SRC)
