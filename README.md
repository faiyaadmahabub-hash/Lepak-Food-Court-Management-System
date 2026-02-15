# Lepak Food Court Management System 🍔

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com)
[![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)](https://netbeans.apache.org/)
[![Swing](https://img.shields.io/badge/Swing-007396?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)

A comprehensive Java-based Food Ordering and Delivery Management System designed to streamline operations in food courts. This application implements Object-Oriented Programming principles and provides role-based dashboards for Customers, Vendors, Delivery Runners, Admins, and Managers.

## 📖 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [System Architecture](#system-architecture)
- [User Roles](#user-roles)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [OOP Concepts](#oop-concepts)
- [Team Members](#team-members)
- [Screenshots](#screenshots)
- [Future Enhancements](#future-enhancements)

## 🎯 Overview

The **Lepak Food Court Management System** is a digital platform designed to boost efficiency in food ordering and delivery processes. The system enables:

- 📱 Customers to browse menus, place orders, and track deliveries
- 🍳 Vendors to manage menu items and process orders
- 🚚 Delivery runners to accept tasks and update delivery status
- 👤 Admins to manage users and handle transactions
- 📊 Managers to monitor performance and resolve complaints

### Key Highlights

- **Credit System**: Customers use wallet balance instead of cash payments
- **Order Tracking**: Real-time order status updates
- **Review System**: Customers can review orders and delivery runners
- **Revenue Dashboard**: Vendors and delivery runners can track earnings
- **Multi-Delivery Options**: Dine-in, Regular Delivery, and Express Delivery

---

## ✨ Features

### Customer Features
- 👥 User registration and authentication
- 💰 Wallet-based payment system
- 🛒 Browse menu items from multiple vendors
- 📦 Place orders with delivery options (Dine-in/Regular/Express)
- 🔍 Track order status in real-time
- 📝 View order history
- ⭐ Review orders and delivery runners (1-5 star rating)
- 📜 View transaction history
- ❌ Cancel pending orders with automatic refunds

### Vendor Features
- 🍽️ Manage menu items (Add/Remove/Update)
- 📋 View incoming orders
- ✅ Accept or reject orders
- 👨‍🍳 Update order preparation status
- 🚴 Assign delivery runners to orders
- 💵 Generate sales revenue reports
- 👀 View customer reviews

### Delivery Runner Features
- 📥 View assigned delivery tasks
- ✔️ Accept or decline delivery requests
- 🔄 Update delivery status (In Transit/Delivered/Failed)
- 📊 View task history
- 💸 Track earnings with revenue dashboard
- ⭐ View customer reviews

### Admin Features
- 👥 Manage all users (Edit/Delete)
- 🏪 View registered vendors
- 🚚 View active delivery runners
- 💳 Top-up customer wallet balance
- 🧾 Generate transaction receipts
- 📧 Send receipts to customers

### Manager Features
- 📈 Monitor system performance
- 📊 View delivery success rates
- 🔍 Track total orders and delivered orders
- 🆘 Resolve customer complaints

---

## 🏗️ System Architecture

The system follows the **Model-View-Controller (MVC)** architectural pattern:

```
FoodOrderingSystem2.0/
│
├── src/
│   ├── model/              # Data models and business logic
│   │   ├── User.java       # Abstract base class
│   │   ├── Customer.java
│   │   ├── Vendor.java
│   │   ├── DR.java         # Delivery Runner
│   │   ├── Manager.java
│   │   ├── Admin.java
│   │   ├── Order.java
│   │   ├── MenuItem.java
│   │   ├── OrderItem.java
│   │   ├── Transaction.java
│   │   ├── DeliveryInfo.java
│   │   ├── OrderStatus.java
│   │   └── OrderType.java
│   │
│   ├── view/               # GUI components (Swing)
│   │   ├── LoginFrame.java
│   │   ├── SignUpFrame.java
│   │   ├── CustomerDashboard.java
│   │   ├── VendorDashboard.java
│   │   ├── DeliveryDashboard.java
│   │   ├── AdminDashboard.java
│   │   ├── ManagerDashboard.java
│   │   └── ... (other UI components)
│   │
│   ├── controller/         # Business logic controllers
│   │   ├── UserController.java
│   │   ├── OrderController.java
│   │   ├── MenuController.java
│   │   ├── VendorController.java
│   │   ├── CustomerController.java
│   │   ├── DRController.java
│   │   ├── AdminController.java
│   │   └── ManagerController.java
│   │
│   └── util/               # Utility classes
│       └── FileHandler.java
│
├── data/                   # Text file storage
│   ├── users.txt
│   ├── menu_items.txt
│   ├── orders.txt
│   └── transactions.txt
│
└── Images/                 # UI assets
```

### Class Diagram Overview

The system uses inheritance hierarchy with `User` as the abstract base class, extended by:
- **Customer**: Manages wallet balance and order history
- **Vendor**: Handles menu items and orders
- **DR (Delivery Runner)**: Manages delivery tasks
- **Manager**: Monitors system performance
- **Admin**: System-wide user management

---

## 👥 User Roles

| Role | Prefix | Capabilities |
|------|--------|-------------|
| **Customer** | CUS | Order food, track deliveries, manage wallet, write reviews |
| **Vendor** | VD | Manage menu, accept/reject orders, assign delivery runners |
| **Delivery Runner** | DR | Accept deliveries, update status, track earnings |
| **Admin** | AD | User management, wallet top-ups, generate receipts |
| **Manager** | MA | Performance monitoring, complaint resolution |

---

## 🛠️ Technologies Used

- **Programming Language**: Java (JDK 8+)
- **GUI Framework**: Java Swing
- **IDE**: Apache NetBeans
- **Data Storage**: Text files (`.txt`)
- **Design Pattern**: Model-View-Controller (MVC)
- **Build Tool**: Apache Ant (via NetBeans)

---

## 📥 Installation

### Prerequisites

- Java Development Kit (JDK 8 or higher)
- Apache NetBeans IDE (or any Java IDE)
- Git (for cloning the repository)

### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/FoodOrderingSystem2.0.git
   cd FoodOrderingSystem2.0
   ```

2. **Open in NetBeans**
   - Launch NetBeans IDE
   - Click `File` → `Open Project`
   - Navigate to the cloned directory
   - Select `FoodOrderingSystem2.0` and click `Open Project`

3. **Build the Project**
   - Right-click on the project in the Projects panel
   - Select `Clean and Build`

4. **Run the Application**
   - Right-click on the project
   - Select `Run`
   - The Login window will appear

### Data Files Setup

The system will automatically create the following data files on first run:
- `data/users.txt` - User credentials and information
- `data/menu_items.txt` - Vendor menu items
- `data/orders.txt` - Order records
- `data/transactions.txt` - Transaction history

---

## 🚀 Usage

### First Time Setup

1. **Register as a Customer**
   - Click "Sign Up" on the login page
   - Fill in user details (Username, Password, Name, Mobile, Address)
   - Select role: CUSTOMER
   - Click "Register"

2. **Login**
   - Enter your UserID and Password
   - Click "Login"
   - You'll be redirected to your role-specific dashboard

### Sample User Credentials (from documentation)

| UserID | Password | Role | Name |
|--------|----------|------|------|
| CUS001 | 123 | Customer | Emmanuel |
| VD001 | 765 | Vendor | Batrisya |
| DR001 | 4567 | Delivery Runner | Faiyad |
| AD001 | 234 | Admin | Nurul |
| MA001 | 000 | Manager | Ehung |

### Common Workflows

#### **Ordering Food (Customer)**
1. Login as Customer
2. Click "View Menu" to browse available items
3. Click "Place Order" and enter:
   - Menu Item ID (e.g., IT002)
   - Quantity
   - Delivery service (Dine-in/Regular/Express)
4. Order is saved and wallet balance is deducted
5. Track order status using "Check Order Status"

#### **Processing Orders (Vendor)**
1. Login as Vendor
2. View incoming orders in the order table
3. Select order and click "Accept Order"
4. Order status changes to "PREPARING"
5. Click "Assign Delivery Runner" to assign a DR
6. Generate revenue report using "Generate Sales Report"

#### **Delivery Management (Delivery Runner)**
1. Login as Delivery Runner
2. View assigned deliveries in the dashboard
3. Click "Accept Delivery" to confirm task
4. Update status using "Update Status" (In Transit → Delivered)
5. View earnings in "Revenue Dashboard"

#### **Admin Operations**
1. Login as Admin
2. **Manage Users**: Edit customer information
3. **Top-Up Credit**: Add balance to customer wallets
4. **Generate Receipt**: Create transaction receipts
5. **View Vendors/Runners**: Monitor active users

---

## 📂 Project Structure

```
FoodOrderingSystem2.0/
│
├── build/                  # Compiled classes
├── dist/                   # Executable JAR
├── lib/                    # External libraries
│   └── absolutelayout/     # NetBeans layout library
├── nbproject/              # NetBeans project files
├── src/
│   ├── controller/         # 8 controller classes
│   ├── model/              # 13 model classes
│   ├── view/               # 18 view classes + forms
│   ├── util/               # FileHandler utility
│   └── Images/             # 60+ UI images
├── data/                   # Runtime data files
├── build.xml               # Ant build script
├── manifest.mf             # JAR manifest
└── README.md               # This file
```

### Key Files

- **FileHandler.java**: Handles all file I/O operations
- **User.java**: Abstract base class implementing polymorphism
- **Order.java**: Core order management logic
- **LoginFrame.java**: Application entry point

---

## 🎓 OOP Concepts Implemented

### 1. **Encapsulation**
- Private fields with public getters/setters
- Example: `Customer.walletBalance` accessed via `getWalletBalance()`

### 2. **Inheritance**
- `User` abstract class extended by `Customer`, `Vendor`, `DR`, `Manager`, `Admin`
- Code reuse through common attributes (userID, password, role, name, etc.)

### 3. **Polymorphism**
- Abstract method `getUserType()` overridden in each subclass
- Dynamic method dispatch at runtime

### 4. **Abstraction**
- `User` class as abstract template
- Interface-like behavior through abstract methods

### 5. **Association**
- `Order` contains `Customer`, `Vendor`, and `List<OrderItem>`
- `OrderItem` links `MenuItem` with quantity

### 6. **Composition**
- `Order` has-a `DeliveryInfo`
- `Order` has-many `OrderItem`

### 7. **File Handling**
- Persistent data storage using BufferedReader/BufferedWriter
- CRUD operations on text files

### 8. **Exception Handling**
- Try-catch blocks for IOException
- Input validation and error messages

---

## 👨‍💻 Team Members

**Group 15 - APU2F2411CS(DA/DF)**

| Student ID | Name | Role Distribution |
|------------|------|-------------------|
| TP077983 | Faiyad Mahabub Tasin (Leader) | 20% each role |
| TP055673 | Batrisya Binti Mohammad Iqmal | 20% each role |
| TP079311 | Loh Ehung | 20% each role |
| TP078574 | Emmanuel Naranendhya Tyatan | 20% each role |
| TP079309 | Nurul Aiman Binti Raizal Azhar | 20% each role |

- **Course**: CT038-3-2-OODJ (Object Oriented Development with Java)
- **Institution**: Asia Pacific University of Technology & Innovation

---

## 📸 Screenshots

### Login & Registration
- User-friendly login interface
- Role-based registration with dropdown selection

### Customer Dashboard
- Wallet balance display
- Order history table with status tracking
- Menu browsing and order placement
- Review submission system

### Vendor Dashboard
- Two-panel view: Orders table and Menu items table
- Order acceptance/rejection functionality
- Menu item management (Add/Remove)
- Sales revenue reporting

### Delivery Runner Dashboard
- Assigned delivery tasks table
- Accept/Decline delivery options
- Status update dropdown (Delivered/In Transit/Failed)
- Earnings tracking

### Admin Dashboard
- User management interface
- Wallet top-up functionality
- Transaction receipt generation
- Vendor/Runner listing

### Manager Dashboard
- Performance monitoring reports
- Delivery success rate calculation
- Complaint resolution interface

---

## 🔮 Future Enhancements

1. **Database Integration**
   - Migrate from text files to MySQL/PostgreSQL
   - Improve data integrity and querying capabilities

2. **Real-time Notifications**
   - Push notifications for order updates
   - SMS/Email alerts for customers

3. **Payment Gateway**
   - Integrate online payment options (Stripe, PayPal)
   - QR code payment support

4. **Advanced Analytics**
   - Dashboard with charts and graphs
   - Sales forecasting and trend analysis

5. **Mobile Application**
   - Android/iOS companion apps
   - Cross-platform synchronization

6. **Enhanced Security**
   - Password encryption (BCrypt/SHA-256)
   - Two-factor authentication
   - Role-based access control (RBAC)

7. **Map Integration**
   - Google Maps API for delivery tracking
   - Route optimization for delivery runners

8. **Multi-language Support**
   - Internationalization (i18n)
   - Support for Malay, Chinese, Tamil

9. **Advanced Search & Filters**
   - Filter menu by category, price, ratings
   - Search functionality for all entities

10. **Promotional System**
    - Discount codes and vouchers
    - Loyalty points program

---

## 📄 License

This project is developed as part of an academic assignment for **Object Oriented Development with Java (CT038-3-2-OODJ)** at Asia Pacific University.

**Academic Use Only** - Not licensed for commercial distribution.

---

## 🙏 Acknowledgments

- **Asia Pacific University** for providing the educational framework
- **NetBeans Community** for the IDE and Swing tools
- **Group 15 Team Members** for collaborative development
- **Instructors** for guidance and feedback throughout the project

---

## 📞 Contact

**Project Documentation**: Refer to `GROUP 15.pdf` for detailed technical documentation, use case diagrams, and screenshots.

---

## 🐛 Known Issues

1. **Data Consistency**: Text file storage may cause data inconsistency in concurrent access
2. **Limited Validation**: Some input fields lack comprehensive validation
3. **UI Responsiveness**: Interface may not scale well on high-DPI displays
4. **Error Handling**: Some edge cases may not be fully handled

**Note**: These issues are documented for future improvement and learning purposes.

---

## 📚 Documentation

Comprehensive documentation including:
- Class diagrams
- Use case diagrams
- Activity diagrams
- Detailed feature explanations
- Screenshot gallery

Can be found in the `GROUP 15.pdf` file (50 pages).

---

**Built with ❤️ by Group 15 | APU 2024**
