package app.jaba.dtos;

public record ErrorResponseDto(String message, String method, String path, String timestamp, int status) {
}
