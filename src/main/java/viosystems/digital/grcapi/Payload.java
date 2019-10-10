package viosystems.digital.grcapi;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payload {
    Set<Node> nodes;
    Set<Link> links;
}
