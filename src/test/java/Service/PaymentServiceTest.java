package Service;

import com.cosmeticPlatform.CosmeticPlatform.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.cosmeticPlatform.CosmeticPlatform.repository.PaymentRepository;
import com.cosmeticPlatform.CosmeticPlatform.service.PaymentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPayment() {
        Payment payment = new Payment();
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment savedPayment = paymentService.addPayment(payment);

        assertNotNull(savedPayment);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testGetPaymentById() {
        Long id = 1L;
        Payment payment = new Payment();
        payment.setId(id);

        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));

        Payment foundPayment = paymentService.getPaymentById(id);

        assertNotNull(foundPayment);
        assertEquals(id, foundPayment.getId());
        verify(paymentRepository, times(1)).findById(id);
    }

    @Test
    void testGetPaymentById_NotFound() {
        Long id = 1L;

        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentService.getPaymentById(id);
        });

        assertEquals("Ödeme bulunamadı" + id, exception.getMessage());
        verify(paymentRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllPayments() {
        Payment payment1 = new Payment();
        Payment payment2 = new Payment();
        List<Payment> paymentList = Arrays.asList(payment1, payment2);

        when(paymentRepository.findAll()).thenReturn(paymentList);

        List<Payment> allPayments = paymentService.getAllPayment();

        assertNotNull(allPayments);
        assertEquals(2, allPayments.size());
        verify(paymentRepository, times(1)).findAll();
    }
}
