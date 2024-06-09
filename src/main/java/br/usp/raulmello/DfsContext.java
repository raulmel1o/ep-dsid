package br.usp.raulmello;

import br.usp.raulmello.utils.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DfsContext {
    private Address motherNode;
    private Address previousNode;
    private Address nextNode;
    private Address activeNeighbor;
    private List<Address> eligibleNeighbors;
}
