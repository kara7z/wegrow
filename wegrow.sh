#!/bin/bash
# WeGrow helper: ./wegrow.sh start|stop|status|deploy|log
PAYARA="${PAYARA_HOME:-$HOME/Documents/servers/payara6}"
ADMIN="$PAYARA/bin/asadmin"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

if [ ! -x "$ADMIN" ]; then
  echo "Payara not found at $PAYARA. Install it there or set PAYARA_HOME." >&2
  exit 1
fi

case "$1" in
  start)
    "$ADMIN" --port 4849 start-domain ;;
  stop)
    "$ADMIN" --port 4849 stop-domain ;;
  status)
    "$ADMIN" --port 4849 list-domains 2>&1 | head -1
    curl -s http://localhost:8081/wegrow/api/health || true
    echo ;;
  deploy)
    cd "$SCRIPT_DIR" || exit 1
    mvn -q -DskipTests package || exit 1
    "$ADMIN" --port 4849 deploy --name wegrow --force target/wegrow.war ;;
  log)
    tail -f "$PAYARA/glassfish/domains/domain1/logs/server.log" ;;
  *)
    echo "usage: ./wegrow.sh start|stop|status|deploy|log" ;;
esac
