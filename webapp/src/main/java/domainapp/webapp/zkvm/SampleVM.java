package domainapp.webapp.zkvm;

import domainapp.modules.simple.dom.impl.SimpleObjectRepository;
import lombok.Getter;
import lombok.Setter;
import org.zkoss.bind.annotation.Command;

import javax.inject.Inject;

public class SampleVM {

    @Getter @Setter
    private String name;

    @Command
    public void btnClick() {
        simpleObjectRepository.create(name);
    }

    @Inject
    SimpleObjectRepository simpleObjectRepository;
}
