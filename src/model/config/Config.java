package model.config;

import java.util.Map;

public record Config(PortConfig console, PortConfig pus) {
    private Map<String, PortConfig> ports() {
        return Map.of(
            "console", console,
            "pus", pus
        );
    }

    public PortConfig getPort(String name) {
        return ports().get(name);
    }
}
