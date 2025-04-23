package uz.uat.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StatusCountDto {
    private String status;
    private Long count;

}
