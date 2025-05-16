package net.tylerwade.registrationsystem.prerequisites;

import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.prerequisites.dto.ManagePrerequisiteRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PrerequisiteService {

    List<Prerequisite> findAllByCourseId(Long courseId);

    Prerequisite create(Long courseId, ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException;

    Prerequisite update(Long courseId, Long prerequisiteId, ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException;

    void delete(Long prerequisiteId) throws HttpRequestException;
}
