package net.tylerwade.registrationsystem.enrollment;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnrollmentId implements Serializable {
    private Long studentId;
    private Long courseSectionId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EnrollmentId that = (EnrollmentId) o;
        return Objects.equals(studentId, that.studentId) && Objects.equals(courseSectionId, that.courseSectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, courseSectionId);
    }
}
