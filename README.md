# 💰 Smart Expense Splitter

A console-based Java application that helps groups split expenses fairly and calculates optimized settlements to minimize transactions.

## 🎯 Features

- **Add Multiple People** - Create a group with any number of participants
- **Track Expenses** - Record who paid for what and who should split the cost
- **Real-time Balance Tracking** - See who owes money and who is owed
- **Optimized Settlement Calculation** - Minimizes the number of transactions needed to settle all debts
- **Precise Money Handling** - Uses BigDecimal to avoid floating-point errors

## 🛠️ Technologies & Concepts

- **Java Core**
- **BigDecimal** for precise financial calculations
- **Collections Framework** (HashMap, ArrayList, List)
- **Object-Oriented Programming** (Encapsulation, Classes)
- **Greedy Algorithm** for optimizing settlements

## 📋 Prerequisites

- Java JDK 8 or higher
- IntelliJ IDEA (or any Java IDE)

## 🚀 How to Run

1. Clone this repository
```bash
git clone https://github.com/abhigyaabs2e/Smart-Expense-Splitter.git
```

2. Open the project in IntelliJ IDEA

3. Run `SmartExpenseSplitter.java`

4. Follow the console menu to manage expenses

## 💡 Usage Example

```
1. Add person: Alice
2. Add person: Bob
3. Add person: Charlie

4. Add expense:
   - Description: Dinner
   - Amount: $90
   - Paid by: Alice
   - Split among: Alice, Bob, Charlie
   (Each person owes $30)

5. Add expense:
   - Description: Taxi
   - Amount: $30
   - Paid by: Bob
   - Split among: Bob, Charlie
   (Each person owes $15)

6. Calculate settlements:
   💰 Charlie owes Alice: $30.00
   💰 Bob owes Alice: $15.00
```

## 📊 How It Works

1. **Expense Recording** - When an expense is added, the app calculates each person's share
2. **Balance Updates** - Each person's balance is updated (positive = owed money, negative = owes money)
3. **Settlement Optimization** - The algorithm groups creditors and debtors, then matches them to minimize total transactions

## 🎓 Learning Outcomes

This project demonstrates:
- Financial precision with BigDecimal
- Data structure manipulation
- Algorithm optimization
- Menu-driven console application design
- Clean code practices

## 📝 Menu Options

1. Add person
2. Add expense
3. Show all expenses
4. Show balances
5. Calculate settlements
6. Exit

## 🤝 Contributing

Feel free to fork this project and submit pull requests for any improvements!

## 📄 License

This project is open source and available under the MIT License.

---

⭐ If you found this project helpful, please give it a star!
