package model.config;

import java.util.Map;

public record TargetConfig(String bootloader, String flash) {
    private Map<String, String> paths() {
        return Map.of(
            "bootloader", bootloader,
            "flash", flash
        );
    }

    public String getPath(String name) {
        return paths().get(name);
    }
}
