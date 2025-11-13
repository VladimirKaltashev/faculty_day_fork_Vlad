package ru.tbank.education.school.lesson2.bank

import jdk.jfr.DataAmount

class Bank {
    var accountSeq = 1
    var clientSeq = 1

    val customers: MutableList<Client> = mutableListOf()
    val accounts: MutableList<Account> = mutableListOf()

    fun addClient(clientFullName: String) {
        val newClient = Client(
            id = "C-${clientSeq}",
            fullName = clientFullName,
        )
        clientSeq++
        customers.add(newClient)
    }

    fun addAccount(clientId: String) {
        val newAccount = Account(
            id = "A-${accountSeq}",
            balance = 0.0,
            customerId = clientId
        )
        accountSeq++
        accounts.add(newAccount)
    }

    fun transfer(fromAccountId: String, toAccountId: String, amount: Double) {
        val fromAccount = accounts.find { it.id == fromAccountId }!!
        val toAccount = accounts.find { it.id == toAccountId }!!

        val ok = fromAccount.withdraw(amount)

        if (ok) {
            toAccount.deposit(amount)
        }


    }

}