package com.axiell.ehub.provider.elib.library3;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
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

    @Before
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
        assertFalse(result);
    }

    @Test
    public void product2_available() throws Exception {
        result = underTest.matches(ID_2);
        assertTrue(result);
    }
}
