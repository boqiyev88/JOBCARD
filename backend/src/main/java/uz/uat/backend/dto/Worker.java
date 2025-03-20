package uz.uat.backend.dto;

import lombok.Builder;

@Builder
public record Worker(String name ,boolean isChecked) {
}
