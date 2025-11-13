package ru.tbank.education.school.lesson2.Pizzeria

fun main() {
    println("=== Pizzeria System Demo ===\n")

    val pizzeria = Pizzeria("Tasty Pizza")

    val customer_1 = Customer(
        name = "Ivan Petrov",
        address = "Kotlinskaya st, 15",
        phone = "+79991234567"
    )

    val customer_2 = Customer(
        name = "Alexey Potanin",
        address = "Kolobok st, 3",
        phone = "+79991234598"
    )

    println("1. Creating order...")
    val order_1 = pizzeria.createOrder(1, customer_1)
    val order_2 = pizzeria.createOrder(2, customer_1)

    println("\n2. Adding pizzas to order...")
    val margherita = Pizza("Margherita", 400.0, PizzaSize.MEDIUM)
    val pepperoni = Pizza("Pepperoni", 500.0, PizzaSize.LARGE)
    val specialPizza = SpecialPizza("Chef's Special", 600.0, PizzaSize.LARGE, Ingredient.TRUFFLE_OIL)
    val fries = FrenchFries(
        "FrenchFries",
        300.0,
        FriesSize.LARGE
    )

    order_1.addItem(margherita)
    order_1.addItem(pepperoni)
    order_2.addItem(specialPizza)
    order_2.addItem(fries)


    println("\n3. Order details:")
    println("Total amount: ${order_1.totalPrice} rub.")
    println("Total amount: ${order_2.totalPrice} rub.")

    println("\n4. Processing order...")
    pizzeria.processOrder(order_1)
    pizzeria.processOrder(order_2)

    println("\n5. Final order status:")
    println("Order #1: ${order_1.status}")
    println("Order #2: ${order_2.status}")
    println("\n=== Demo completed ===")
}