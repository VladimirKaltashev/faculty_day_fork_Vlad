package ru.tbank.education.school.lesson2.Pizzeria

fun main() {
    println("=== Pizzeria System Demo ===\n")

    val pizzeria = Pizzeria("Tasty Pizza")

    val customer = Customer(
        name = "Ivan Petrov",
        address = "Kotlinskaya st, 15",
        phone = "+79991234567"
    )

    println("1. Creating order...")
    val order = pizzeria.createOrder(1, customer)

    println("\n2. Adding pizzas to order...")
    val margherita = Pizza("Margherita", 400.0, PizzaSize.MEDIUM)
    val pepperoni = Pizza("Pepperoni", 500.0, PizzaSize.LARGE)
    val specialPizza = SpecialPizza("Chef's Special", 600.0, PizzaSize.LARGE, Ingredient.TRUFFLE_OIL)

    order.addItem(margherita)
    order.addItem(pepperoni)
    order.addItem(specialPizza)

    println("\n3. Order details:")
    println("Total amount: ${order.totalPrice} rub.")

    println("\n4. Processing order...")
    pizzeria.processOrder(order)

    println("\n5. Final order status: ${order.status}")
    println("\n=== Demo completed ===")
}