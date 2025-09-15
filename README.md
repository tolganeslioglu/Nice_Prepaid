

# Prepaid & Credit Card Management App

This project is a **JavaFX-based desktop application** developed during the COMP200 Summer Internship.  
It simulates a **digital wallet system** where customers can manage prepaid and credit cards, perform top-ups, send money, and view transaction history.  

---

## 🚀 Features
- **Login & Registration**
  - Secure login with TCKN + passcode
  - New customer registration with validation (TCKN, email, phone, age, passcode rules)

- **Prepaid Card Management**
  - View card details (card number, CVV, expiration, balance)
  - Top-up balance using linked credit card
  - Send money between prepaid cards
  - View transaction history

- **Credit Card Management**
  - Apply for a credit card
  - View credit card details (limit, available credit, debt, due date, etc.)
  - Use credit card to top-up prepaid card balance

- **Transaction System**
  - Stores and loads transactions from JSON files (`*_transactions.json`)
  - Records top-ups, money transfers, deposits, and withdrawals

---

## 🛠️ Technologies Used
- **Java 21**
- **JavaFX 24** (UI development with FXML & SceneBuilder)
- **Maven** (dependency management & build system)
- **Jackson (databind, core, annotations)** (JSON parsing and serialization)
- **JUnit 4.13.2** (testing)
- **Charm Glisten** (UI components)

---

## 📂 Project Structure
```
├── src/main/java/
│   ├── controllers/        # JavaFX Controllers (LoginController, HomeController, etc.)
│   ├── models/             # Core classes (Card, PrepaidCard, CreditCard, Customer, etc.)
│   └── utils/              # Helper classes (SaveChangesToJson, etc.)
├── src/main/resources/
│   ├── fxml/               # UI layouts (Login.fxml, Home.fxml, etc.)
│   └── assets/             # Images, logos, etc.
├── credentials.json        # Customer credentials
├── prepaidcards.json       # Prepaid card data
├── creditcards.json        # Credit card data
├── 1001_transactions.json  # Example transaction file (per customer)
├── 1010_transactions.json  # Example transaction file (per customer)
```

---

## ⚙️ Setup & Run

### Prerequisites
- JDK 21
- Maven 3.8+
- Internet connection (for Maven dependencies)

### Run with Maven
```bash
mvn clean javafx:run
```

### Build a Runnable JAR
```bash
mvn clean package
java -jar target/nice_prepaid-1.0-SNAPSHOT-shaded.jar
```

---

## 📦 Future Improvements
- Replace JSON storage with a real database (e.g., MySQL or PostgreSQL)
- Add encryption for sensitive customer and card data
- Improve UI/UX with responsive layouts
- Add unit tests for controllers and models

---

## 📄 License
This project is developed as part of **COMP200 Summer Practice**  
© 2025 Tolga Neslioğlu