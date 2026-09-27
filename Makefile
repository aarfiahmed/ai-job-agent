up:
	docker compose up -d postgres qdrant n8n

down:
	docker compose down

run:
	mvn spring-boot:run

test:
	mvn test

package:
	mvn clean package
