package viosystems.digital.grcapi;

import java.util.*;
import java.util.stream.Collectors;

import org.neo4j.driver.*;
import org.neo4j.driver.types.Relationship;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrcQueriesController {

    private final Driver driver;

    public GrcQueriesController(Driver driver) {
        this.driver = driver;
    }

    @GetMapping(path = "/graph/{service}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Payload getServiceMajorReleaseGraph(@PathVariable("service")String service ) {

        Payload payload = Payload.builder().nodes(new HashSet<>()).links(new HashSet<>()).build();
        try (Session session = driver.session()) {
           session.run("MATCH(s:ITService {name:{name}}) - [e:EXPOSES] -> (r:Risk)-[m:MITIGATED_BY]->(c:Control {major : \"Mandatory\"})-[g:CONTROL_GATE ]->(cg)<-[a:ACCOUNTABLE]-(p) RETURN *", Collections.singletonMap("name", service))
                    .stream().forEach(r -> addToPayload(r, payload));

        }
        return  payload;
    }

    @GetMapping(path = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getServices() {
        try (Session session = driver.session()) {
            return session.run("MATCH(s:ITService) RETURN s.name")
                    .stream().map(r -> r.get("s.name").asString()).collect(Collectors.toList());

        }
    }


    private void addToPayload(Record r, Payload payload) {
        payload.getNodes().add(makeNode(r.get("s").asNode()));
        payload.getLinks().add(makeLink(r.get("e").asRelationship()));
        payload.getNodes().add(makeNode(r.get("r").asNode()));
        payload.getLinks().add(makeLink(r.get("m").asRelationship()));
        payload.getNodes().add(makeNode(r.get("c").asNode()));
        payload.getLinks().add(makeLink(r.get("g").asRelationship()));
        payload.getNodes().add(makeNode(r.get("cg").asNode()));
        payload.getLinks().add(makeLink(r.get("a").asRelationship()));
        payload.getNodes().add(makeNode(r.get("p").asNode()));
    }

    private Link makeLink(Relationship r) {

        return Link.builder()
                .sid(r.startNodeId())
                .tid(r.endNodeId())
                .name(r.type())
                .build();
    }

    private Node makeNode(org.neo4j.driver.types.Node n) {
        return Node.builder()
                .name(n.get("name").asString())
                .id(n.id())
                .build();
    }


}
