./mvnw flyway:clean -Dflyway.user=user -Dflyway.password=password -Dflyway.schemas=public
./mvnw flyway:migrate -Dflyway.user=user -Dflyway.password=password -Dflyway.schemas=public
