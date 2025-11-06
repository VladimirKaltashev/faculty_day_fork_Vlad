package ru.tbank.education.school.lesson1

/**
 * Сумма четных чисел.
 */
fun sumEvenNumbers(numbers: Array<Int>) =
    numbers.filter { it % 2 == 0}.sum()

fun main() {
    val numbers = arrayOf(1, 2, 3, 4, 5, 6)
    val sumEven = sumEvenNumbers(numbers)
    println(sumEven)
}
//fun sumEvenNumbers(numbers: Array<Int>): Int {
//    var sum = 0
//    for (elem in numbers) {
//        if (elem % 2 == 0) sum += elem
//    }
//    return sum
//}
