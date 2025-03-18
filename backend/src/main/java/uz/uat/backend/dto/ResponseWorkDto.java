package uz.uat.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import uz.uat.backend.model.Employeer;


import java.util.List;

@Builder
public record ResponseWorkDto(
        @NotBlank String jobCard_id,
        @NotBlank String service_id,
        @NotBlank String threshold,
        @NotBlank String repeat_int,
        @NotBlank String zone,
        @NotBlank String mrf,
        @NotBlank String access,
        @NotBlank String airplane_app,
        @NotBlank String access_note,
        @NotBlank String task_description,
        @NotBlank boolean dit,
        @NotBlank Employeer workers_names,
        @NotBlank List<TaskDto> taskList) {
}