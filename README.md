# 🚀 Enterprise E2E Automation Framework

An enterprise-grade, full-stack automation framework demonstrating advanced testing architectures across both Frontend (UI) and Backend (API) layers.

Built from scratch to showcase modern QA engineering practices, including Data-Driven Testing, POJO Serialization, and True Hybrid API-to-UI Handoffs.

## 🏗️ Tech Stack & Architecture

* **Language:** Java 25
* **UI Automation:** Selenium WebDriver 4.x
* **API Automation:** RestAssured 5.x
* **Test Runner & Assertions:** TestNG
* **Data Binding (JSON):** Jackson (ObjectMapper / Databind)
* **Data-Driven Testing:** Apache POI (Excel)
* **Build Management:** Apache Maven

## 🔥 Key Engineering Features

1. **Hybrid E2E Integration (The Crown Jewel):** * Bypasses slow UI registration flows by injecting POST requests directly into the backend database via `RestAssured`.
    * Dynamically extracts generated server IDs via Deserialization.
    * Hands the backend ID directly to `Selenium WebDriver` to seamlessly log into the frontend UI.
2. **Advanced Data Architecture:** * Complete elimination of hardcoded JSON strings.
    * Utilizes pure Java **POJOs** (Plain Old Java Objects) mapped with Jackson `@JsonIgnoreProperties` for flawless Serialization and Deserialization.
3. **Data-Driven Assembly Lines:** * Integrated TestNG `@DataProvider` with `Apache POI` to read `.xlsx` files, enabling the framework to dynamically generate hundreds of API requests or UI logins from a single test method.
4. **Cloud-Ready Security:** * Implements custom Header injection (API Keys, User-Agent spoofing) to bypass cloud firewalls (e.g., Cloudflare).

## ⚙️ How to Run Locally

### Prerequisites
* JDK 17 or higher installed.
* Maven installed and added to your System PATH.
* Google Chrome installed.

### Execution
Clone the repository and run the following Maven command from the root directory:

```bash
mvn clean test
```
To run a specific test suite (e.g., the Hybrid API+UI test):
```bash
mvn test -Dtest=HybridE2ETest
```
### 📂 Project Structure
* src/main/java/pojo/ - Contains Data blueprints (UserPayload, UserResponse).

* src/main/java/utils/ - Contains core engine utilities (ExcelReader, ConfigReader).

* src/test/java/ - Contains the actual TestNG execution classes.

* src/test/resources/testdata/ - Holds the .xlsx files driving the framework.

