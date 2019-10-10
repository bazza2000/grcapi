MATCH (r:Risk)-[rel:MITIGATED_BY]->(bits) RETURN *;


MATCH(s:ITService) - [e:EXPOSES] -> (n:Risk)-[d:MITIGATED_BY]->(x:Control {major : "Mandatory"})-[c:CONTROL_GATE ]->(f)<-[a:ACCOUNTABLE]-(role) RETURN *;


MATCH(s:ITService) - [e:EXPOSES] -> (n:Risk) RETURN *;

CALL db.schema();
