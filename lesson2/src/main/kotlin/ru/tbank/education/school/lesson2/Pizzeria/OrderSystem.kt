package ru.tbank.education.school.lesson2.Pizzeria

sealed class OrderStatus {
    object Created : OrderStatus() {
        override fun toString() = "Created"
    }
    object Cooking : OrderStatus() {
        override fun toString() = "Cooking"
    }
    object ReadyForDelivery : OrderStatus() {
        override fun toString() = "Ready for Delivery"
    }
    object OutForDelivery : OrderStatus() {
        override fun toString() = "Out for Delivery"
    }
    object Delivered : OrderStatus() {
        override fun toString() = "Delivered"
    }
}

class Order(
    val orderId: Int,
    val customer: Customer
) {
    private val items = mutableListOf<MenuItem>()
    var status: OrderStatus = OrderStatus.Created

    fun addItem(item: MenuItem) {
        items.add(item)
    }

    val totalPrice: Double
        get() = items.sumOf { it.calculatePrice() }

    fun changeStatus(newStatus: OrderStatus) {
        status = newStatus
    }

    fun processItems(block: (MenuItem) -> Unit) {
        items.forEach { block(it) }
    }
}

class Pizzeria(val name: String) {
    private val orders = mutableListOf<Order>()

    fun createOrder(orderId: Int, customer: Customer): Order {
        val newOrder = Order(orderId, customer)
        orders.add(newOrder)
        return newOrder
    }

    private fun deliverOrder(order: Order) {
        order.changeStatus(OrderStatus.OutForDelivery)
        println("Delivering order #${order.orderId} to address: ${order.customer.address}")
        println("Customer: ${order.customer.name}, phone: ${order.customer.phone}")
        order.changeStatus(OrderStatus.Delivered)
        println("Order #${order.orderId} delivered!")
    }

    fun processOrder(order: Order) {
        order.changeStatus(OrderStatus.Cooking)

        order.processItems { item ->
            when (item) {
                is Pizza -> {
                    item.prepare()
                    item.bake()
                }
                is FrenchFries -> {
                    item.prepare()
                    item.bake()
                }
            }
        }

        order.changeStatus(OrderStatus.ReadyForDelivery)
        deliverOrder(order)
    }
}