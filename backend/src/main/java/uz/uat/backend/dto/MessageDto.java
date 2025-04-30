package uz.uat.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MessageDto {
    private String message;
    private String job_id;

}
