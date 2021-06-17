package tv.codealong.tutorials.springboot.thenewboston.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import tv.codealong.tutorials.springboot.thenewboston.model.Bank
import tv.codealong.tutorials.springboot.thenewboston.service.BankService

@RestController
@RequestMapping("api/banks")
class BankController(private val service: BankService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(ex.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun index(): Collection<Bank> = service.getBanks()

    @GetMapping("/{accountNumber}")
    fun get(@PathVariable accountNumber: String): Bank = service.getBank(accountNumber)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun post(@RequestBody bank: Bank): Bank = service.addBank(bank)
}
