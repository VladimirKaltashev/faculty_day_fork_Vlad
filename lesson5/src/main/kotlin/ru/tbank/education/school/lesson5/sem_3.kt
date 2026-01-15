package ru.tbank.education.school.lesson5

import kotlin.collections.set


/**
 * Простой интерфейс для in-memory кеша, хранящего данные в формате ключ-значение.
 */
interface InMemoryCache<K, V> {
    /**
     * Добавляет пару ключ-значение в кеш.
     *
     * @param key ключ, по которому будет происходить доступ к значению
     * @param value значение, которое нужно сохранить
     * @todo Реализовать добавление элемента в хранилище.
     *       Если ключ уже существует, старое значение должно быть заменено новым.
     */
    fun put(key: K, value: V)

    /**
     * Возвращает значение по указанному ключу.
     *
     * @param key ключ, по которому нужно найти значение
     * @return значение, связанное с ключом, или null, если ключ не найден
     * @todo Реализовать получение значения по ключу.
     *       Если ключ отсутствует в кеше, вернуть null.
     */
    fun get(key: K): V?

    /**
     * Удаляет элемент из кеша по ключу.
     *
     * @param key ключ, который нужно удалить
     * @return true, если элемент был найден и удалён; false — если ключ не существовал
     * @todo Реализовать удаление элемента по ключу.
     *       Вернуть true, если элемент был удалён, иначе — false.
     */
    fun remove(key: K): Boolean

    /**
     * Проверяет, содержится ли указанный ключ в кеше.
     *
     * @param key ключ для проверки
     * @return true, если ключ существует в кеше; иначе — false
     * @todo Реализовать проверку наличия ключа.
     *       Убедиться, что проверяется только наличие ключа (без учёта срока жизни и т.п.).
     */
    fun containsKey(key: K): Boolean

    /**
     * Очищает весь кеш — удаляет все элементы.
     *
     * @todo Реализовать полную очистку хранилища.
     *       После вызова size() должен вернуть 0, а isEmpty() — true.
     */
    fun clear()

    /**
     * Возвращает количество элементов в кеше.
     *
     * @return текущее число пар ключ-значение
     * @todo Реализовать подсчёт количества элементов в хранилище.
     */
    fun size(): Int

    /**
     * Проверяет, пуст ли кеш.
     *
     * @return true, если в кеше нет ни одного элемента; иначе — false
     * @todo Реализовать проверку на пустоту.
     *       Эквивалентно size() == 0, но может быть реализовано эффективнее.
     */
    fun isEmpty(): Boolean
}

class SimpleInMemoryCache<K, V> : InMemoryCache<K, V> {
    private val storage = mutableMapOf<K, V>()

    override fun put(key: K, value: V) {
        storage[key] = value
    }

    override fun get(key: K): V? {
        if (storage.containsKey(key)) {
            return storage[key]
        } else {
            return null
        }
    }

    override fun remove(key: K): Boolean {
        if (storage.containsKey(key)) {
            storage.remove(key)
            return true
        } else {
            return false
        }
    }

    override fun containsKey(key: K): Boolean {
        if (storage.containsKey(key)) {
            return true
        } else {
            return false
        }
    }

    override fun clear() {
        storage.clear()
    }

    override fun size(): Int {
        return storage.size
    }

    override fun isEmpty(): Boolean {
        return storage.isEmpty()
    }
}