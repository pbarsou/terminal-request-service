COMPOSE=./docker-compose.yaml
COMPOSE_INFRA=./docker-compose.infra.yaml

up:
	docker compose -f $(COMPOSE) up -d

up-infra:
	docker compose -f $(COMPOSE_INFRA) up -d

start:
	docker compose -f $(COMPOSE) start

start-infra:
	docker compose -f $(COMPOSE_INFRA) start

stop:
	docker compose -f $(COMPOSE) stop

stop-infra:
	docker compose -f $(COMPOSE_INFRA) stop

down:
	docker compose -f $(COMPOSE) down -v --remove-orphans

down-infra:
	docker compose -f $(COMPOSE_INFRA) down -v --remove-orphans

run:
	./mvnw spring-boot:run

verify:
	./mvnw clean verify

unit:
	./mvnw clean test

it-integrated:
	./mvnw clean verify -DskipUnitTests=true -DskipPactTests=true -DskipCoreITs=false

format:
	./mvnw fmt:format

install:
	./mvnw clean install

package:
	./mvnw clean package -DskipUnitTests=true -DskipPactTests=true -DskipCoreITs=true