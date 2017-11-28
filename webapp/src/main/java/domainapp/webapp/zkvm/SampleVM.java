package domainapp.webapp.zkvm;

import domainapp.modules.simple.dom.impl.SimpleObject;
import domainapp.modules.simple.dom.impl.SimpleObjectRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.isis.core.runtime.system.context.IsisContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelList;

import javax.inject.Inject;
import javax.lang.model.util.SimpleElementVisitor6;
import java.util.List;
import java.util.concurrent.Callable;

public class SampleVM {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private ListModelList<SimpleObject> simpleObjects;

    @Init
    public void init(){
        simpleObjects = new ListModelList<>(listSimpleObject());
    }

    @Command
    public void btnClick() {
        SimpleObject simpleObject = SimpleObject.create(name);
        createSimpleObject(simpleObject);
        simpleObjects.add(simpleObject);
    }

    @Command
    public void deleteSimpleObject(@BindingParam("simpleObject") final SimpleObject simpleObject){
        deleteSimpleObjects(simpleObject);
        simpleObjects.remove(simpleObject);
    }

    private List<SimpleObject> listSimpleObject() {

        return IsisContext.getSessionFactory().doInSession(new Callable<List<SimpleObject>>() {
            @Override
            public List<SimpleObject> call() {

                final SimpleObjectRepository simpleObjectRepository = IsisContext.getSessionFactory()
                        .getServicesInjector().lookupService(SimpleObjectRepository.class);

                return simpleObjectRepository.listAll();
            }
        });
    }

    private SimpleObject createSimpleObject(final SimpleObject simpleObject) {

        return IsisContext.getSessionFactory().doInSession(new Callable<SimpleObject>() {
            @Override
            public SimpleObject call() {

                final SimpleObjectRepository simpleObjectRepository = IsisContext.getSessionFactory()
                        .getServicesInjector().lookupService(SimpleObjectRepository.class);

                return simpleObjectRepository.create(simpleObject);
            }
        });
    }

    private void deleteSimpleObjects(final SimpleObject simpleObject) {

        IsisContext.getSessionFactory().doInSession(new Callable<Void>() {
            @Override
            public Void call() {

                final SimpleObjectRepository simpleObjectRepository = IsisContext.getSessionFactory()
                        .getServicesInjector().lookupService(SimpleObjectRepository.class);

                simpleObjectRepository.delete(simpleObject);
                return null;
            }
        });
    }
}
