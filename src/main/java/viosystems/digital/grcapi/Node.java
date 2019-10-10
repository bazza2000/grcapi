package viosystems.digital.grcapi;

import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    @EqualsAndHashCode.Include
    long id;
    String name;
}
