# Modular Monolith Architecture with ArchUnit Enforcement


> **Status:** 🟢 Accepted 
> **Date:** 2026-07-29
> **ID:** #03

---

## Context
The application is being developed bt a small development team, with one main developer and a supporting developer/mentor. 
The system is also currently limited in scope and does not justify the operational and architectural complexity associated with multiple independently deployable services.

The application therefore needs an architecture that keeps the operational complexity of a monolith while providing stronger structural boundaries than a traditional layered monolith.
A traditional layered monolith would be relatively simple to implement, but would provide limited practice with defining and maintaining boundaries between distinct areas of the system. A modular structure provides explicit boundaries between modules and encourages loose coupling while keeping the application within a single deployable unit.


## Decision
We will use the modular monolithic architecture to build the application.
The application will consist of separate modules with defined boundaries and controlled dependencies, between them. To enforce the architectural boundaries, we have chosen to use ArchUnit, an open-source java library for testing architectural constraints.


## Consequences

### Positive
- The application retains the simplicity and relatively low operational cost of a single deployable unit.
- Explicit module boundaries and controlled dependencies provide stronger separation and loose coupling than a conventional layered monolith.
- The modular structure provides practical experience with architectural boundaries and dependency management that are also relevant to more complex architectural styles

### Trade-offs
- Modules cannot be deployed, scaled or operated independently.
- The module boundaries and ArchUnit rules must be maintained as the system evolves, although generic rules will be used where possible to automatically enforce constraints for new modules


