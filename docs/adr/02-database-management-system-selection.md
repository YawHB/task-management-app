# Database Management System Selection


> **Status:** 🟢 Accepted 
> **Date:** 2026-07-23 
> **ID:** #002  

---

## Context
This project serves as a portfolio piece demonstrating proficiency with industry-standard technologies.    
Selecting a DBMS widely adopted in production environments ensures the skills and patterns learned remain directly applicable to professional software development roles. Therefore Selecting an industry relevant DBMS is of high priority.  
Furthermore, the entities within our task management application have natural relational characteristics, hence the database choice should reflect this.


## Decision
We will use an RDBMS, with PostgreSQL as the selected SQL variant. 
 
This decision satisfies the requirement for industry adoption while accommodating the task management domain's inherent entity relationships. Among RDBMS options, PostgreSQL was chosen for its widespread use in production environments, making it a strong signal to potential employers of hands-on experience with a modern, enterprise-grade database.



## Consequences

### Positive
- Enforces disciplined data modeling, building transferable skills for production systems.
- Provides ACID compliance and strong data integrity out of the box.
- Widely supported by hosting providers and DevOps toolchains.


### Trade-offs
- Requires upfront schema design, adding initial planning overhead
- Future modifications demand migration scripts to preserve data consistency
- Less flexible for rapidly evolving, unstructured data patterns compared to NoSQL alternatives






