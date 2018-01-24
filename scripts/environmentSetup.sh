#!/usr/bin/env bash

function copyEnvVarsToLocalProperties {
    LOCAL_PROPERTIES=$HOME"/locafarm/local.properties"
    ANDROID_MANIFEST=$HOME"/locafarm/app/src/main/AndroidManifest.xml"

    export LOCAL_PROPERTIES
    echo "Local Properties should exist at $LOCAL_PROPERTIES"

    export ANDROID_MANIFEST
    echo "AndroidManifest should exist at $ANDROID_MANIFEST"

    sed -i -e "s/\"\${google_map_api_key}\"/"\"$GOOGLE_MAP_API_KEY\""/g" $ANDROID_MANIFEST


    if [ ! -f "$LOCAL_PROPERTIES" ]; then
        echo "Gradle local does not exist"

        echo "Creating local Properties file..."
        touch $LOCAL_PROPERTIES

        echo "Writing GOOGLE_MAP_API_KEY to local.properties..."
        echo "googlemapapikey=$GOOGLE_MAP_API_KEY" > $LOCAL_PROPERTIES

    fi

}
