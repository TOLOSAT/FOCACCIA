package model.config;

import java.util.Map;

public record TargetConfig(String bootloader, String path) {
    private Map<String, String> paths() {
        return Map.of(
            "bootloader", bootloader,
            "path", path
        );
    }

    public String getPath(String name) {
        return paths().get(name);
    }
}
