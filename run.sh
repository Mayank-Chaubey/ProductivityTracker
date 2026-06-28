#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

command="${1:-up}"
docker_port="${DOCKER_PORT:-8081}"

compose() {
  if docker compose version >/dev/null 2>&1; then
    docker compose "$@"
  elif command -v docker-compose >/dev/null 2>&1; then
    docker-compose "$@"
  else
    echo "Docker Compose is required for '$command'. Install Docker Desktop, then run this script again." >&2
    exit 1
  fi
}

case "$command" in
  up|start|run)
    echo "Starting ProductivityTracker at http://localhost:${docker_port}/ProductivityTracker"
    compose up --build
    ;;
  detached|background)
    echo "Starting ProductivityTracker in the background at http://localhost:${docker_port}/ProductivityTracker"
    compose up --build -d
    ;;
  local)
    port="${PORT:-8080}"
    echo "Starting ProductivityTracker locally at http://localhost:${port}/ProductivityTracker"
    mvn -q -DskipTests test-compile dependency:build-classpath -Dmdep.outputFile=target/runtime-classpath.txt
    filtered_classpath="$(tr ':' '\n' < target/runtime-classpath.txt \
      | grep -Ev '/org/eclipse/jetty/|/org/mortbay/|/org/apache/ant/|/org/apache/maven/|/org/eclipse/aether/|/org/eclipse/sisu/|/org/sonatype/|/javax/|/aopalliance/|/com/google/guava/|/org/codehaus/plexus/|/org/apache/httpcomponents/|/commons-|/org/ow2/asm/|/com/thoughtworks/qdox/|/org/apache/taglibs/|/jakarta/servlet/jsp/jakarta.servlet.jsp-api/|/jakarta/servlet/jakarta.servlet-api/' \
      | paste -sd ':' -)"
    java \
      -Dport="${port}" \
      -cp "target/classes:target/test-classes:${filtered_classpath}" \
      com.productivitytracker.LocalTomcatServer
    ;;
  local-detached)
    if ! command -v screen >/dev/null 2>&1; then
      echo "screen is required for local-detached. Run './run.sh local' instead." >&2
      exit 1
    fi
    screen -dmS productivitytracker bash -lc "cd '$PWD' && ./run.sh local > server_run.log 2>&1"
    echo "Started local server in screen session 'productivitytracker'."
    echo "App URL: http://localhost:${PORT:-8080}/ProductivityTracker"
    ;;
  local-stop)
    if command -v screen >/dev/null 2>&1; then
      screen -S productivitytracker -X quit >/dev/null 2>&1 || true
    fi
    if lsof -tiTCP:"${PORT:-8080}" -sTCP:LISTEN >/dev/null 2>&1; then
      kill "$(lsof -tiTCP:"${PORT:-8080}" -sTCP:LISTEN)"
    fi
    ;;
  stop|down)
    compose down
    ;;
  restart)
    compose down
    echo "Restarting ProductivityTracker at http://localhost:${docker_port}/ProductivityTracker"
    compose up --build
    ;;
  logs)
    compose logs -f app
    ;;
  build)
    mvn -q -DskipTests package
    ;;
  clean)
    compose down -v
    ;;
  *)
    cat <<USAGE
Usage: ./run.sh [command]

Commands:
  up         Build and run MySQL + the web app in the foreground (default)
  detached  Build and run in the background
  local      Run with embedded Tomcat 10; requires MySQL on localhost:3306
  local-detached
             Run local mode in a detached screen session
  local-stop Stop the detached local server
  stop       Stop containers
  restart    Rebuild and restart containers
  logs       Follow app container logs
  build      Build the WAR locally with Maven
  clean      Stop containers and remove the database volume
USAGE
    exit 1
    ;;
esac
