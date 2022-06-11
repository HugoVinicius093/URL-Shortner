package com.hugo.urlshortner.validation;

import com.hugo.urlshortner.model.dto.UrlDTO;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;

@Slf4j
public class UrlEntityDtoValidationTest {

    private static final String ORIGINAL_URL = "originalUrl";

    private static Validator validator;

    private static UrlDTO urlDTO;

    private static Set<ConstraintViolation<UrlDTO>> constraintViolations;

    private static List<String> validUrls;

    @BeforeAll
    public static void setUp() {
        log.info("--- STARTING TESTS ON VALIDATION ---");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        createListOfValidUrls();
    }

    private static void createListOfValidUrls() {
        validUrls = Arrays.asList(
                "www.google.com",
                "www.facebook.com",
                "https://www.linkedin.com/",
                "https://www.google.com/maps/dir/53.2292906,-6.6573972/Kildare+Village,+Kildare,+Nurney+Road,+County+Kildare/@53.1844134,-6.8532482,12z/data=!3m1!4b1!4m10!4m9!1m1!4e1!1m5!1m1!1s0x485d78d993d39b51:0x1c41b501c26d7b8d!2m2!1d-6.9167071!2d53.1540269!3e3"
        );
    }

    @BeforeEach
    public void beforeEach() {
        urlDTO = UrlDTO.builder()
                .originalUrl("www.facebook.com")
                .build();
    }

    @AfterEach
    public void resetConstraintViolations() {
        constraintViolations = new HashSet<>();
    }

    @Test
    void shouldHaveNoViolations() {
        constraintViolations = validator.validate(urlDTO);
        describeViolations(constraintViolations);
        Assertions.assertTrue(constraintViolations.isEmpty());
    }

    @Test
    void shouldHaveViolations() {
        urlDTO.setOriginalUrl(null);

        constraintViolations = validator.validate(urlDTO);
        describeViolations(constraintViolations);
        Assertions.assertTrue(constraintViolations.size() > 0);
    }

    private void describeViolations(Set<ConstraintViolation<UrlDTO>> constraintViolations) {
        if (constraintViolations.size() > 0) {
            log.info("Fields:");
            for (ConstraintViolation<UrlDTO> violation : constraintViolations) {
                log.info(violation.getMessage());
            }
        } else {
            log.info("Object fully valid");
        }
    }

    @Test
    void shouldFailValidationWithInvalidInputValueForOriginalUrlField() {
        constraintViolations = validator.validateValue(UrlDTO.class, ORIGINAL_URL, "");
        MatcherAssert.assertThat("Original URL field should have a validation error for invalid input", constraintViolations.size(), is(1));
        MatcherAssert.assertThat(constraintViolations.iterator().next().getMessage(), is("Invalid URL!"));
    }

    @Test
    void shouldNotFailValidationWithValidInputValueForOriginalUrlField() {
        constraintViolations = validator.validateValue(UrlDTO.class, ORIGINAL_URL, "www.facebook.com.br/test");
        MatcherAssert.assertThat("Original URL field should have a validation error for invalid input", constraintViolations.size(), is(0));
    }

}
