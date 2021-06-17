package tv.codealong.tutorials.springboot.thenewboston.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import tv.codealong.tutorials.springboot.thenewboston.model.Bank

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    val baseUrl = "/api/banks"

    @Nested
    @DisplayName("GET /api/bank")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBanks {

        @Test
        fun `should return all banks`() {
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].accountNumber") { value("1234") }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/bank/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank {

        @Test
        fun `should return the bank with the given account number`() {
            val accountNumber = 1234
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust") { value("3.14") }
                    jsonPath("$.transactionFee") { value("17") }
                }
        }

        @Test
        fun `should return NOT FOUND if the account number does not exist`() {
            val accountNumber = "does_not_exist"
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank {

        @Test
        fun `should add the new bank`() {
            val newBank = Bank("acc123", 31.45, 2)

            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.accountNumber") { value(newBank.accountNumber) }
                    jsonPath("$.trust") { value(newBank.trust) }
                    jsonPath("$.transactionFee") { value(newBank.transactionFee) }
                }
        }

        @Test
        fun `should return BAD REQUEST if bank with given account number already exists`() {
            // given
            val invalidBank = Bank("1234", 1.0, 1)

            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }
            performPost
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }
}
