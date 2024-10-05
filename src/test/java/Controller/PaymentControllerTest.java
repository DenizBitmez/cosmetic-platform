package Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cosmeticPlatform.CosmeticPlatform.controller.PaymentController;
import com.cosmeticPlatform.CosmeticPlatform.model.Payment;
import com.cosmeticPlatform.CosmeticPlatform.model.request.PaymentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.cosmeticPlatform.CosmeticPlatform.service.PaymentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PaymentControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testAddPayment() throws Exception {
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setName("Test Payment");
        paymentRequestDTO.setAmount(100.0);
        paymentRequestDTO.setId(1L);

        Payment payment = new Payment();
        payment.setName(paymentRequestDTO.getName());
        payment.setId(paymentRequestDTO.getId());
        payment.setAmount(paymentRequestDTO.getAmount());

        when(paymentService.addPayment(any(Payment.class))).thenReturn(payment);

        mockMvc.perform(post("/api/payment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(paymentRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Payment"))
                .andExpect(jsonPath("$.amount").value(100.0));

        verify(paymentService, times(1)).addPayment(any(Payment.class));
    }

    @Test
    void testGetPaymentById() throws Exception {
        Long id = 1L;
        Payment payment = new Payment();
        payment.setId(id);
        payment.setName("Test Payment");
        payment.setAmount(100.0);

        when(paymentService.getPaymentById(id)).thenReturn(payment);

        mockMvc.perform(get("/api/payment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Payment"))
                .andExpect(jsonPath("$.amount").value(100.0));

        verify(paymentService, times(1)).getPaymentById(id);
    }

    @Test
    void testGetAllPayments() throws Exception {
        Payment payment1 = new Payment();
        payment1.setId(1L);
        payment1.setName("Payment 1");
        payment1.setAmount(50.0);

        Payment payment2 = new Payment();
        payment2.setId(2L);
        payment2.setName("Payment 2");
        payment2.setAmount(75.0);

        List<Payment> paymentList = Arrays.asList(payment1, payment2);

        when(paymentService.getAllPayment()).thenReturn(paymentList);

        mockMvc.perform(get("/api/payment/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Payment 1"))
                .andExpect(jsonPath("$[1].name").value("Payment 2"));

        verify(paymentService, times(1)).getAllPayment();
    }
}
