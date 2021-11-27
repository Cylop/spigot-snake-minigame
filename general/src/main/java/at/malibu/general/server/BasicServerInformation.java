package at.malibu.general.server;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BasicServerInformation implements ServerInformation {

    private String name;
    private String prefix;

}
