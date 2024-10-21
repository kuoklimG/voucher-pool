package com.kl.voucher_pool;

import com.kl.voucher_pool.model.Recipient;
import com.kl.voucher_pool.repository.RecipientRepository;
import com.kl.voucher_pool.repository.SpecialOfferRepository;
import com.kl.voucher_pool.config.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
class DataLoaderTest {

    @Mock
    private RecipientRepository recipientRepository;

    @Mock
    private SpecialOfferRepository specialOfferRepository;

    @InjectMocks
    private DataLoader dataLoader;

    @BeforeEach
    void setUp() {
        reset(recipientRepository);
        reset(specialOfferRepository);
    }

    @Test
    void testLoadRecipients_WhenEmpty() {
        when(recipientRepository.count()).thenReturn(0L);
        when(recipientRepository.saveAll(anyList())).thenReturn(List.of(mock(Recipient.class), mock(Recipient.class), mock(Recipient.class)));

        dataLoader.run();

        verify(recipientRepository, times(1)).count();
        verify(recipientRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testLoadRecipients_WhenNotEmpty() {
        when(recipientRepository.count()).thenReturn(3L);

        dataLoader.run();

        verify(recipientRepository, times(2)).count();
        verify(recipientRepository, never()).saveAll(anyList());
    }

    @Test
    void testLoadSpecialOffers_WhenEmpty() {
        when(recipientRepository.count()).thenReturn(0L);
        when(recipientRepository.saveAll(anyList())).thenReturn(List.of(mock(Recipient.class), mock(Recipient.class), mock(Recipient.class)));

        dataLoader.run();

        verify(specialOfferRepository, times(1)).count();
        verify(specialOfferRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testLoadSpecialOffers_WhenNotEmpty() {
        when(specialOfferRepository.count()).thenReturn(3L);

        dataLoader.run();

        verify(specialOfferRepository, times(2)).count();
        verify(specialOfferRepository, never()).saveAll(anyList());
    }
}
