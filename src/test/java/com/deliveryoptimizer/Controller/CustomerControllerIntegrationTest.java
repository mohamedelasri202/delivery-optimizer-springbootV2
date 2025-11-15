package com.deliveryoptimizer.Controller;

import com.deliveryoptimizer.Controller.CustomerController;
import com.deliveryoptimizer.Model.Customer;
import com.deliveryoptimizer.Repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    private static final String ENDPOINT_URL = "/api/customers/search-by-time-slot";
    private static final String TIME_SLOT_MATCH = "10:00-12:00";
    private static final String TIME_SLOT_MISMATCH = "14:00-16:00";

    @BeforeEach
    void setUp() {

        customerRepository.deleteAll();


        insertCustomer("Alice Smith", TIME_SLOT_MATCH);
        insertCustomer("Bob Johnson", TIME_SLOT_MATCH);
        insertCustomer("Charlie Brown", TIME_SLOT_MATCH);
        insertCustomer("David Wilson", TIME_SLOT_MATCH);


        insertCustomer("Eve Taylor", TIME_SLOT_MISMATCH);
        insertCustomer("Frank Miller", TIME_SLOT_MISMATCH);

    }

    private void insertCustomer(String name, String preferredTimeSlot) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPreferredTimeSlot(preferredTimeSlot);
        customer.setAddress("Test Address");
        customer.setLatitude(0.0);
        customer.setLongitude(0.0);
        customerRepository.save(customer);
    }


    @Test
    void testBasicFilterAndPagination() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("timeSlot", TIME_SLOT_MATCH)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortedBy", "name")
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isOk())


                .andExpect(jsonPath("$.totalElements", is(4)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.number", is(0)))


                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.content[0].name", is("Alice Smith")));
    }


    @Test
    void testCustomPageSizeAndSecondPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("timeSlot", TIME_SLOT_MATCH)
                        .param("page", "1")
                        .param("size", "2")
                        .param("sortedBy", "name")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())


                .andExpect(jsonPath("$.totalElements", is(4)))
                .andExpect(jsonPath("$.totalPages", is(2)))
                .andExpect(jsonPath("$.size", is(2)))
                .andExpect(jsonPath("$.number", is(1)))

                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].name", is("Charlie Brown")));
    }


    @Test
    void testNoResultsFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL)
                        .param("timeSlot", "00:00-01:00")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortedBy", "name")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.totalElements", is(0)))
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.empty", is(true)));
    }
}