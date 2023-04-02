#!/bin/bash

echo "rs-init.sh starting"

DELAY=10

mongo <<EOF
var config = {
    "_id": "dbrs",
    "version": 1,
    "members": [
        {
            "_id": 1,
            "host": "mongo-db-primary:27017",
            "priority": 3
        },
        {
            "_id": 2,
            "host": "mongo-db-secondary-1:27017",
            "priority": 2
        },
        {
            "_id": 3,
            "host": "mongo-db-secondary-2:27017",
            "priority": 1
        }
    ]
};
rs.initiate(config, { force: true });
rs.status();
EOF

echo "Sleeping during ${DELAY} seconds for replicaset config to be applied."

sleep $DELAY

mongo < /scripts/init.sh