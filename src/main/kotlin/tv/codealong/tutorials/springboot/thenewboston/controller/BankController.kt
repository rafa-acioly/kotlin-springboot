package tv.codealong.tutorials.springboot.thenewboston.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @PatchMapping
    fun patch(@RequestBody bank: Bank): Bank = service.updateBank(bank)
}
