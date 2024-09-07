package shared.dto;

import lombok.*;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GradeDto {
    private int id;
    private UserDto studentDto;
    private SubjectDto subjectDto;
    private float grade;
    private LocalDate date;
}
