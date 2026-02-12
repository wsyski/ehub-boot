package com.axiell.ehub.local.provider.elib.library3;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductMatcherTest {
    public static final String ID_1 = "1";
    public static final String ID_2 = "2";
    private ProductMatcher underTest;
    private List<BookAvailability.Product> products;
    @Mock
    private BookAvailability.Product product1;
    @Mock
    private BookAvailability.Product product2;
    private boolean result;

    @BeforeEach
    public void setUpUnderTest() {
        given(product1.getProductId()).willReturn(ID_1);
        given(product1.isAvailable()).willReturn(false);
        given(product2.getProductId()).willReturn(ID_2);
        given(product2.isAvailable()).willReturn(true);
        products = Lists.newArrayList(product1, product2);
        underTest = new ProductMatcher(products) {
            @Override
            boolean condition(BookAvailability.Product product) {
                return product.isAvailable();
            }
        };
    }

    @Test
    public void product1_notAvailable() throws Exception {
        result = underTest.matches(ID_1);
        Assertions.assertFalse(result);
    }

    @Test
    public void product2_available() throws Exception {
        result = underTest.matches(ID_2);
        Assertions.assertTrue(result);
    }
}
