package br.cefetmg.lsi.bimasco.core.problems.candidatesList;

import br.cefetmg.lsi.bimasco.core.problems.VehicleRoutingTimeWindowProblem;
import br.cefetmg.lsi.bimasco.core.solutions.element.VRTWElement;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VRTWCandidatesList extends CandidatesList<VehicleRoutingTimeWindowProblem, VRTWElement> {

    RandomDataGenerator rnd;

    public VRTWCandidatesList(VehicleRoutingTimeWindowProblem problem) {
        super(problem);
        rnd = new RandomDataGenerator();
    }

    @Override
    public List<VRTWElement> getCandidates() {
        int clients = problem.getClients();
        int trucks = rnd.nextInt(0, clients - 1);
        List<VRTWElement> clientList = IntStream.rangeClosed(1, clients)
                .boxed().map(VRTWElement::new).collect(Collectors.toList());

        List<VRTWElement> trucksList = Collections.nCopies(trucks, 0)
                .stream().map(VRTWElement::new)
                .collect(Collectors.toList());

        clientList.addAll(trucksList);

        return clientList;
    }
}
