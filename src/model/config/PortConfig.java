package model.config;

public record PortConfig(String port, Integer baud, Integer dataBits, Integer stopBits, Integer parity) {}
