# Country Service

This repository contains a Country Service application.

## Prerequisites

Before running the application locally, ensure you have the following installed:

- Java Development Kit (JDK) 21
- Maven
- Docker (optional, if you prefer running the application in a Docker container)

## Getting Started

Follow these steps to run the application on your local machine:

1. Clone this repository:

    ```bash
    git clone https://github.com/gariptipici/countryservice.git
    ```

2. Navigate to the project directory:

    ```bash
    cd countryservice
    ```

3. Build the application using Maven:

    ```bash
    mvn clean install
    ```

4. Run the application:

    ```bash
    mvn spring-boot:run
    ```

5. Once the application has started, you can access it at `http://localhost:8080`.

   `Sample request`: Sorted list of countries by population density in descending order.
    ```bash
    curl --location 'http://localhost:8080/region/asia/countries/mostBorderingCountryOfADifferentRegion'
    ```
   `Sample request`: Country in Asia containing the most bordering countries of a different region.
    ```bash
    curl --location 'http://localhost:8080/all/countries/byPopulationDensity'
    ```

## Running with Docker (Optional)

If you prefer running the application in a Docker container, follow these additional steps:

1. Build the Docker image:

    ```bash
    docker build -t countryservice .
    ```

2. Run the Docker container:

    ```bash
    docker run -p 8080:8080 countryservice
    ```

3. The application will be accessible at `http://localhost:8080`.

    ```bash
    curl --location 'http://localhost:8080/region/asia/countries/mostBorderingCountryOfADifferentRegion'
    ```
    ```bash
    curl --location 'http://localhost:8080/all/countries/byPopulationDensity'
    ```

## Running with Docker Compose (Optional)


> Every push to this repository will automatically trigger a build and push of the Docker image to Docker Hub. This ensures that docker-compose up will provide the latest version of the application.

If you prefer running the application in a Docker container, follow these additional steps:

1. Run the Docker container:

    ```bash
    docker-compose up
    ```

2. The application will be accessible at `http://localhost:8080`.

    ```bash
    curl --location 'http://localhost:8080/region/asia/countries/mostBorderingCountryOfADifferentRegion'
    ```
    ```bash
    curl --location 'http://localhost:8080/all/countries/byPopulationDensity'
    ```

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=gariptipici_countryservice)](https://sonarcloud.io/summary/new_code?id=gariptipici_countryservice)

