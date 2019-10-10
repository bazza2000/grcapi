package viosystems.digital.grcapi;

import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    @EqualsAndHashCode.Include
    long sid;
    @EqualsAndHashCode.Include
    long tid;
    String name;
}
