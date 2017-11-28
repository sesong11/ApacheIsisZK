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
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import javax.inject.Inject;
import javax.lang.model.util.SimpleElementVisitor6;
import java.util.List;
import java.util.concurrent.Callable;

public class SampleVM {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String notes;

    @Getter @Setter
    private SimpleObject simpleObject;

    @Getter @Setter
    private ListModelList<SimpleObject> simpleObjects;

    @Init
    public void init(){
        simpleObjects = new ListModelList<>(listSimpleObject());
    }

    @Command
    public void createObject() {
        SimpleObject simpleObject = SimpleObject.create(name);
        simpleObject.setNotes(notes);
        SimpleObject s = createSimpleObject(simpleObject);
        simpleObject.setId(s.getId());
        simpleObjects.add(simpleObject);
    }

    @Command
    @NotifyChange({"simpleObjects"})
    public void updateObject() {
        SimpleObject object = this.simpleObject;
        object.setName(name);
        object.setNotes(notes);
        updateSimpleObjects(object);
    }

    @Command
    @NotifyChange({"name", "notes"})
    public void selectObject(@BindingParam("simpleObject")final SimpleObject simpleObject){
        this.simpleObject = simpleObject;
        name = simpleObject.getName();
        notes = simpleObject.getNotes();
    }

    @Command
    public void deleteObject(@BindingParam("simpleObject") final SimpleObject simpleObject){
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

                SimpleObject simpleObject1 = simpleObjectRepository.create(simpleObject);
                return simpleObject1;
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

    private void updateSimpleObjects(final SimpleObject simpleObject) {
        IsisContext.getSessionFactory().doInSession(new Callable<Void>() {
            @Override
            public Void call() {

                final SimpleObjectRepository simpleObjectRepository = IsisContext.getSessionFactory()
                        .getServicesInjector().lookupService(SimpleObjectRepository.class);

                simpleObjectRepository.update(simpleObject);
                return null;
            }
        });
    }
}
