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

class FrenchFries(
    name: String,
    basePrice: Double,
    val size: FriesSize
) :
    MenuItem(name, basePrice) {
    override fun calculatePrice(): Double {
        return when(size) {
            FriesSize.SMALL -> basePrice * 0.8
            FriesSize.MEDIUM -> basePrice
            FriesSize.LARGE -> basePrice * 1.4
        }
    }

    fun prepare() {
        println("Preparing fries '$name' size $size")
    }
}

class Burger(
    name: String,
    basePrice: Double,
) :
    MenuItem(name, basePrice) {
    override fun calculatePrice(): Double {
        return basePrice
    }
}

enum class PizzaSize {
    SMALL, MEDIUM, LARGE
}

enum class FriesSize {
    SMALL, MEDIUM, LARGE
}

enum class Ingredient(val price: Double) {
    DOUBLE_CHEESE(50.0),
    SPICY_PEPPERONI(70.0),
    TRUFFLE_OIL(150.0),
    EXTRA_VEGGIES(40.0),
    BACON(60.0)
}