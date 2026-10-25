# API Architectural Style


> **Status:** 🟢 Accepted 
> **Date:** 2026-10-24
> **ID:** #05 

---

## Context
The application needs a way to communicate between the client and server. 
The read/write operations identified in the functional requirements indicate that the application will be mostly focused on relatively simple operations. 
This favours an approach that is simple to implement without introducing unnecessary complexity. 
This preference is further supported by the expected limited number of API endpoints and relatively small payloads.

Finally, the chosen style should support interoperability, since a potential frontend technology has yet to be determined.


## Decision
We will use REST APIs as our API architectural style, 
in order for the client to do read and write operations to the database.



## Consequences

### Positive
- Simplicity: REST builds on standard HTTP methods, status codes, and conventions, 
  making it straightforward to implement and understand.
- Interoperability: REST APIs are widely supported across programming languages, 
  technology stacks, and development tools, allowing different systems to communicate.
- Frontend flexibility: The frontend technology has yet to be determined. 
  REST allows the frontend to be developed using different technologies without requiring the backend 
  to use the same technology stack.
- Familiarity: HTTP and REST conventions are widely understood by developers,
  making the API accessible to a broad range of developers and tools.

### Trade-offs
- Response flexibility: REST endpoints typically define their response representations on the server. 
  This can result in clients receiving unnecessary data or needing additional requests to retrieve related 
  information. Appropriate DTOs and endpoint design can mitigate these issues.
- API contract management: REST does not prescribe a formal schema for defining endpoints, request bodies, 
  and response bodies. A separate specification, such as OpenAPI, is needed if a formal API contract is desired. 
  This introduces an additional decision and requires the specification to be maintained.
