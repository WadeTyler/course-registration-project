package net.tylerwade.registrationsystem.term;

import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.dto.ManageTermRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class TermServiceImplTest {

    @Mock
    private TermRepository termRepository;


    @InjectMocks
    private TermServiceImpl termService;

    private List<Term> mockTerms;

    @BeforeEach
    void setup() {

        Term mockTerm1 = Term.builder()
                .id(1L)
                .registrationStart(LocalDate.now())
                .registrationEnd(LocalDate.now().plusWeeks(1))
                .startDate(LocalDate.now().plusWeeks(1).plusDays(1))
                .endDate(LocalDate.now().plusWeeks(9))
                .build();

        mockTerms = List.of(mockTerm1);
    }

    @Test
    void findById_ExistingTerm_ReturnsTerm() throws HttpRequestException {
        // Arrange
        Mockito.when(termRepository.findById(1L)).thenReturn(Optional.ofNullable(mockTerms.getFirst()));

        // Act & Assert
        Term existingTerm = termService.findById(1L);

        Assertions.assertEquals(1L, existingTerm.getId());
        Assertions.assertTrue(existingTerm.isRegistrationOpen());
    }

    @Test
    void create_TermDatesExist_ThrowsHttpRequestException() {
        // Arrange
        ManageTermRequest manageTermRequest = new ManageTermRequest(mockTerms.getFirst().getStartDate(), mockTerms.getFirst().getEndDate(), mockTerms.getFirst().getRegistrationStart(), mockTerms.getFirst().getRegistrationEnd());

        Mockito.when(termRepository.existsByStartDateAndEndDate(manageTermRequest.startDate(), manageTermRequest.endDate())).thenReturn(true);

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () -> termService.create(manageTermRequest));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    void create_InvalidDates_ThrowsHttpRequestException() {
        // Arrange
        ManageTermRequest manageTermRequest = new ManageTermRequest(mockTerms.getFirst().getEndDate(), mockTerms.getFirst().getStartDate(), mockTerms.getFirst().getRegistrationStart(), mockTerms.getFirst().getRegistrationEnd());

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () -> termService.create(manageTermRequest));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void create_Valid_ReturnsNewTerm() throws HttpRequestException {
        // Arrange
        ManageTermRequest manageTermRequest = new ManageTermRequest(
                LocalDate.now().plusWeeks(4),
                LocalDate.now().plusWeeks(5),
                LocalDate.now(),
                LocalDate.now().plusWeeks(2)
                );

        Term expectedTerm = Term.builder()
                .startDate(manageTermRequest.startDate())
                .endDate(manageTermRequest.endDate())
                .registrationStart(manageTermRequest.registrationStart())
                .registrationEnd(manageTermRequest.registrationEnd())
                .build();

        Mockito.when(termRepository.existsByStartDateAndEndDate(manageTermRequest.startDate(), manageTermRequest.endDate())).thenReturn(false);
        Mockito.when(termRepository.save(any())).thenReturn(expectedTerm);

        // Act
        Term newTerm = termService.create(manageTermRequest);

        // Assert
        Assertions.assertNotNull(newTerm);
        Assertions.assertEquals(manageTermRequest.startDate(), newTerm.getStartDate());
        Assertions.assertEquals(manageTermRequest.endDate(), newTerm.getEndDate());
        Assertions.assertEquals(manageTermRequest.registrationStart(), newTerm.getRegistrationStart());
        Assertions.assertEquals(manageTermRequest.registrationEnd(), newTerm.getRegistrationEnd());
    }

    @Test
    void update_InvalidDates_ThrowsHttpRequestException() {
        // Arrange
        Long termId = mockTerms.getFirst().getId();

        ManageTermRequest manageTermRequest = new ManageTermRequest(mockTerms.getFirst().getEndDate(), mockTerms.getFirst().getStartDate(), mockTerms.getFirst().getRegistrationStart(), mockTerms.getFirst().getRegistrationEnd());

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () -> termService.update(termId, manageTermRequest));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }


    @Test
    void update_TermDatesExist_ThrowsHttpRequestException() {
        // Arrange
        Long termId = mockTerms.getFirst().getId();

        ManageTermRequest manageTermRequest = new ManageTermRequest(mockTerms.getFirst().getStartDate(), mockTerms.getFirst().getEndDate(), mockTerms.getFirst().getRegistrationStart(), mockTerms.getFirst().getRegistrationEnd());

        Mockito.when(termRepository.findById(termId)).thenReturn(Optional.ofNullable(mockTerms.getFirst()));

        Mockito.when(termRepository.existsByStartDateAndEndDateAndIdNot(manageTermRequest.startDate(), manageTermRequest.endDate(), termId)).thenReturn(true);

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () -> termService.update(termId, manageTermRequest));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    void update_Valid_ReturnsUpdatedTerm() throws HttpRequestException {
        // Arrange
        Long termId = mockTerms.getFirst().getId();

        ManageTermRequest manageTermRequest = new ManageTermRequest(
                LocalDate.now().plusWeeks(4),
                LocalDate.now().plusWeeks(5),
                LocalDate.now(),
                LocalDate.now().plusWeeks(2)
        );

        Mockito.when(termRepository.findById(termId)).thenReturn(Optional.ofNullable(mockTerms.getFirst()));
        Mockito.when(termRepository.existsByStartDateAndEndDateAndIdNot(manageTermRequest.startDate(), manageTermRequest.endDate(), termId)).thenReturn(false);

        Term mockTerm = Term.builder()
                .id(termId)
                .startDate(manageTermRequest.startDate())
                .endDate(manageTermRequest.endDate())
                .registrationStart(manageTermRequest.registrationStart())
                .registrationEnd(manageTermRequest.registrationEnd())
                .build();

        Mockito.when(termRepository.save(any())).thenReturn(mockTerm);

        // Act & Assert
        Term updatedTerm = termService.update(termId, manageTermRequest);

        Assertions.assertNotNull(updatedTerm);
        Assertions.assertEquals(termId, updatedTerm.getId());
        Assertions.assertEquals(updatedTerm.getStartDate(), manageTermRequest.startDate());
        Assertions.assertEquals(updatedTerm.getEndDate(), manageTermRequest.endDate());
        Assertions.assertEquals(updatedTerm.getRegistrationStart(), manageTermRequest.registrationStart());
        Assertions.assertEquals(manageTermRequest.registrationEnd(), updatedTerm.getRegistrationEnd());
    }

    @Test
    void delete_NotFound_ThrowsHttpRequestException() {
        // Arrange
        Long termId = 3L;

        Mockito.when(termRepository.existsById(termId)).thenReturn(false);

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () -> termService.delete(termId));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void delete_Found_DeletesTerm() throws HttpRequestException {
        // Arrange
        Long termId = 1L;

        Mockito.when(termRepository.existsById(termId)).thenReturn(true);

        // Act
        termService.delete(termId);

        // Assert
        verify(termRepository, times(1)).deleteById(termId);
    }


}
