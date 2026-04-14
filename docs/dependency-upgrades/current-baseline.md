# Current Dependency Baseline

Last updated: 2026-04-14

## Core Stack

- Niko Boot Version: 1.1.0-SNAPSHOT
- Java: 21
- Spring Boot Parent: 3.5.9
- Spring Boot BOM: 3.5.9
- Spring Cloud BOM: 2025.0.0
- Spring Cloud Alibaba BOM: 2025.0.0.0

## Key Dependencies

- MyBatis Spring Boot Starter: 3.0.4
- Redisson Spring Boot Starter: 3.52.0
- Spring Security OAuth2 Authorization Server: 1.4.1
- Micrometer Tracing: 1.4.1
- SpringDoc: 2.8.6
- Sa-Token: 1.39.0
- Jackson: 2.18.2
- Jakarta Servlet API: 6.1.0

## Legacy or High-Risk Dependencies

- Apache CXF: 3.2.0
- JJWT: 0.9.1
- Apache POI: 3.15
- Guava: 20.0
- Commons IO: 2.7
- Dozer: 5.4.0
- MySQL Connector: 5.1.46
- SQL Server JDBC: sqljdbc4 4.0

## Build Prerequisites

- Maven Toolchains plugin requires JDK 21 to be defined in `~/.m2/toolchains.xml`.

## Source of Truth

- Parent build and plugin baseline: [niko-boot-parent/pom.xml](/C:/Dev/projects/jee/niko-boot/niko-boot-parent/pom.xml)
- Dependency BOM: [niko-boot-dependencies/pom.xml](/C:/Dev/projects/jee/niko-boot/niko-boot-dependencies/pom.xml)
