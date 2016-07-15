package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.error.EhubExceptionFactoryStub;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.provider.ContentProvider;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class ZinioResponseFactoryTest {
    private static final String LANGUAGE = Locale.ENGLISH.getLanguage();

    @Mock
    protected EhubConsumer ehubConsumer;
    @Mock
    protected ContentProviderConsumer contentProviderConsumer;
    @Mock
    protected ContentProvider contentProvider;

    private IEhubExceptionFactory ehubExceptionFactory = new EhubExceptionFactoryStub();

    private IZinioResponseFactory underTest;

    private IZinioResponse zinioResponse;

    @Before
    public void setUp() {
        underTest = new ZinioResponseFactory();
        ReflectionTestUtils.setField(underTest, "ehubExceptionFactory", ehubExceptionFactory);
    }

    @Test
    public void createLoginResponse() throws Exception {
        String response = givenExpectedZinioResponse("loginResponse.txt");
        zinioResponse = whenGetZinioResponse(response);
        thenResponseStatusOK();
        thenExpectedLoginUrl();
    }

    @Test
    public void createIssuesResponse() throws Exception {
        String response = givenExpectedZinioResponse("issuesResponse.txt");
        zinioResponse = whenGetZinioResponse(response);
        thenResponseStatusOK();
        thenExpectedIssues();
    }

    private void thenExpectedLoginUrl() {
        Assert.assertThat(zinioResponse.getMessage(), Matchers.is("http://www.rbdigitaltest.com/axielltest?p_session=6OY2vUEkd0WzT37ZLkRHrM0VtI8vMccn"));
    }

    private void thenResponseStatusOK() {
        Assert.assertThat(zinioResponse.getStatus(), Matchers.is(ZinioStatus.OK));
    }

    private void thenExpectedIssues() {
        List<IssueDTO> issues = zinioResponse.getAsList(IssueDTO.class);
        Assert.assertThat(issues.size(), Matchers.is(8));
        IssueDTO issueDTO = issues.get(0);
        Assert.assertThat(issueDTO.getId(), Matchers.is("416342560"));
        Assert.assertThat(issueDTO.getTitle(), Matchers.is("July 01, 2015"));
        Assert.assertThat(issueDTO.getImageUrl(), Matchers.is("http://imgs.zinio.com/dag/500601921/2015/416342560/cover.jpg"));
    }

    private IZinioResponse whenGetZinioResponse(final String response) {
        return underTest.create(response, contentProviderConsumer, LANGUAGE);
    }

    private String givenExpectedZinioResponse(final String fileName) throws IOException {
        final String filePath = ZinioResponseFactoryTest.class.getPackage().getName().replace('.', '/') + '/' + fileName;
        return IOUtils.toString(new ClassPathResource(filePath).getInputStream(), StandardCharsets.UTF_8);
    }

}
