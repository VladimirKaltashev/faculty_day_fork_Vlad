package ru.tbank.education.school.lesson8.practise
import kotlin.test.Test
/**
 *
 * Сценарии для тестирования:
 *
 * 1. Позитивные сценарии (happy path):
 *    - Обычный случай: basePrice = 1000, discount = 10%, tax = 20% → проверить корректность формулы.
 *    - Без скидки: discountPercent = 0 → итог = basePrice + налог.
 *    - Без налога: taxPercent = 0 → итог = basePrice минус скидка.
 *    - Без скидки и без налога: итог = basePrice.
 *
 * 2. Негативные сценарии (исключения):
 *    - Отрицательная цена: basePrice < 0 → IllegalArgumentException.
 *    - Скидка вне диапазона: discountPercent < 0 или > 100 → IllegalArgumentException.
 *    - Налог вне диапазона: taxPercent < 0 или > 30 → IllegalArgumentException.
 */

class CalculateFinalPriceTest {
    @Test
    fun common_formula_test() {
        var x = calculateFinalPrice(1000.0, 10, 20).equals(1000*0.9*1.2)
    }

    @Test
    fun common_formula_test_no_discount() {
        var x = calculateFinalPrice(1000.0, 0, 20).equals("saf")
    }


}