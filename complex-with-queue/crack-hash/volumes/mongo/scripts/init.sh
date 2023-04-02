echo "init.sh starting"

use crack-hash-progressing-status
db.createUser({user: 'admin', pwd: 'admin', roles: [ { role: 'dbAdmin', db: 'crack-hash-progressing-status' } ]});