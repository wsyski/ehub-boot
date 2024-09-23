#!/bin/bash

set -v -x
set -u -e

usage() {
  echo "Usage: $0 [-v|--version <version>]"
  exit 1
}

TEMP=$(getopt -o hv: --long help,version:, -n 'set-version' -- "$@")
if [ $? != 0 ]; then usage; fi

eval set -- "$TEMP"

while true; do
  case $1 in
    -h | --help ) usage; shift ;;
    -v | --version ) VERSION="$2"; shift 2 ;;
	  -- ) shift; break;;
    * ) break ;;
  esac
done

if [ -z "$VERSION" ]; then
  echo "Usage: set-version.sh [-v arena-version]"
  usage
fi

mvn versions:set -DnewVersion="$VERSION" && mvn versions:commit

sed -i 's|\(<authinfo.version>\)[^<]*\(</authinfo.version>\)|\1'"$VERSION"'\2|' pom.xml
sed -i '/<parent>/,/<\/parent>/ s|\(<version>\)[^<]*\(</version>\)|\1'"$VERSION"'\2|' integration-tests/tests/remote-2.6/pom.xml

echo "Version set to $VERSION"
