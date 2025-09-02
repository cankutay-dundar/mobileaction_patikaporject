# Patika Air Quality (Spring Boot)

**Run locally**
```bash
export OWM_API_KEY=your_key_here
./mvnw spring-boot:run
# or
mvn clean package && java -jar target/patika-air-quality-0.0.1-SNAPSHOT.jar
```

**Endpoint**
```
GET /api/v1/air-quality?city=ankara&startDate=16-05-2021&endDate=17-05-2021
```
- `city` required; `startDate` / `endDate` optional (defaults to last 7 days)
- Date format: `dd-MM-yyyy`
- Allowed cities: London, Barcelona, Ankara, Tokyo, Mumbai
- Earliest allowed date: 27-11-2020

**Configuration**
- Set `OWM_API_KEY` env var or edit `application.yml` under `app.owm.apiKey`.
- Adjust CAQI thresholds in `application.yml` (`app.caqi.thresholds.*`).

**Docker**
```bash
mvn -q -DskipTests package
docker build -t patika/air-quality:latest .
docker run -p 8080:8080 -e OWM_API_KEY=$OWM_API_KEY patika/air-quality:latest
