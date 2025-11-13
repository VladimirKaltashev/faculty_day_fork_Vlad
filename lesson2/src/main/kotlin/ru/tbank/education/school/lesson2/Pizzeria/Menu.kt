package ru.tbank.education.school.lesson2.Pizzeria

abstract class MenuItem(
    val name: String,
    val basePrice: Double
) {
    abstract fun calculatePrice(): Double
}

open class Pizza(
    name: String,
    basePrice: Double,
    val size: PizzaSize
) :
    MenuItem(name, basePrice) {
    override fun calculatePrice(): Double {
        return when(size) {
            PizzaSize.SMALL -> basePrice * 0.8
            PizzaSize.MEDIUM -> basePrice
            PizzaSize.LARGE -> basePrice * 1.4
        }
    }

    open fun prepare() {
        println("Preparing pizza '$name' size $size")
    }

    fun bake() {
        println("Baking pizza '$name'")
    }
}

class SpecialPizza(
    name: String,
    basePrice: Double,
    size: PizzaSize,
    val specialIngredient: Ingredient
) : Pizza(name, basePrice, size) {
    override fun calculatePrice(): Double {
        return super.calculatePrice() + specialIngredient.price
    }

    override fun prepare() {
        super.prepare()
        println("Adding ${specialIngredient}")
    }
}

enum class PizzaSize {
    SMALL, MEDIUM, LARGE
}

enum class Ingredient(val price: Double) {
    DOUBLE_CHEESE(50.0),
    SPICY_PEPPERONI(70.0),
    TRUFFLE_OIL(150.0),
    EXTRA_VEGGIES(40.0),
    BACON(60.0)
}