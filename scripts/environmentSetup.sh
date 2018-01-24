#!/usr/bin/env bash

function copyEnvVarsToLocalProperties {
    LOCAL_PROPERTIES=$HOME"/locafarm/local.properties"
    export LOCAL_PROPERTIES
    echo "Local Properties should exist at $LOCAL_PROPERTIES"

    if [ ! -f "$LOCAL_PROPERTIES" ]; then
        echo "Gradle local does not exist"

        echo "Creating local Properties file..."
        touch $LOCAL_PROPERTIES

        echo "Writing GOOGLE_MAP_API_KEY to local.properties..."
        echo "googlemapapikey=$GOOGLE_MAP_API_KEY" > $LOCAL_PROPERTIES

    fi
}
