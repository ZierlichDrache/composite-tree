import { of } from 'rxjs';
import { ComponentService } from '../shared/component.service';
import { ComponentNode } from '../shared/dtos';
import { TreeComponent } from './tree.component';
import { fakeAsync, tick } from '@angular/core/testing';


describe('TreeComponent unit', () => {
  let service: ComponentService;
  let component: TreeComponent;

  beforeEach(() => {
    service = {
      getRootComponent: () => { },
      updateRootComponent: (a) => { },
      addNewLeafToComponent: (a) => { },
      deleteComponent: (a) => { },
      updateCompnentValue: (a, b) => { }
    } as ComponentService;
    component = new TreeComponent(service);
  });

  function mockComponentData(id: number, value: number): void {
    (component as any).data = { id, value } as ComponentNode;
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call getRootComponent from service when ngOnInit is called', () => {

    // given
    spyOn(service, 'getRootComponent').and.returnValue(of());

    // when
    component.ngOnInit();

    // then
    expect(service.getRootComponent).toHaveBeenCalled();
  });

  it('should update datasource data from service when it is called', () => {

    // given
    spyOn(service, 'getRootComponent').and.returnValue(of({} as ComponentNode));

    // pre check if initial an empty array
    expect(component.dataSource.data).toEqual([]);

    // when
    component.ngOnInit();

    // then
    expect(component.dataSource.data).not.toEqual([]);
  });

  it('should call expandAll from treecontrol when data from service is received', () => {

    // given
    spyOn(service, 'getRootComponent').and.returnValue(of({} as ComponentNode));
    spyOn(component.treeControl, 'expandAll');

    // when
    component.ngOnInit();

    // then
    expect(component.treeControl.expandAll).toHaveBeenCalled();
  });

  it('should call updateRootComponent from service when saveRootComponentNode is called', () => {

    // given
    spyOn(service, 'updateRootComponent').and.returnValue(of());

    // when
    component.saveRootComponentNode();

    // then
    expect(service.updateRootComponent).toHaveBeenCalled();
  });

  it('should call addNewLeafToComponent from service when onAddNewNodeEvent is called', () => {

    // given
    spyOn(service, 'addNewLeafToComponent').and.returnValue(of());

    // when
    component.onAddNewNodeEvent(1);

    // then
    expect(service.addNewLeafToComponent).toHaveBeenCalled();
  });

  it('should update datasource data from service when addNewLeafToComponent from service is called',  () => {

    // given
    const id = 1;
    const oldValue = 1;
    const expectedNewValue = 2;
    spyOn(service, 'addNewLeafToComponent').and.returnValue(of({id, value: expectedNewValue} as ComponentNode));
    mockComponentData(id, oldValue);

    // when
    component.onAddNewNodeEvent(1);

    // then
    expect(component.dataSource.data[0].value).toBe(expectedNewValue);
  });

  it('should call expandAll from treecontrol when addNewLeafToComponent from service is called and data are received', () => {

    // given
    const id = 1;
    const oldValue = 1;
    const expectedNewValue = 2;
    spyOn(service, 'addNewLeafToComponent').and.returnValue(of({id, value: expectedNewValue} as ComponentNode));
    spyOn(component.treeControl, 'expandAll');
    mockComponentData(id, oldValue);

    // when
    component.onAddNewNodeEvent(1);

    // then
    expect(component.treeControl.expandAll).toHaveBeenCalled();
  });

  it('should call deleteComponent and then getRootComponent from service when onRemoveNodeEvent is called', () => {

    // given
    spyOn(service, 'deleteComponent').and.returnValue(of({} as ComponentNode ));
    spyOn(service, 'getRootComponent').and.returnValue(of());

    // when
    component.onRemoveNodeEvent(1);

    // then
    expect(service.deleteComponent).toHaveBeenCalled();
    expect(service.getRootComponent).toHaveBeenCalled();
  });

  it('should call updateCompnentValue and then getRootComponent from service when onUpdateNodeValueEvent is called', () => {

    // given
    spyOn(service, 'updateCompnentValue').and.returnValue(of({} as ComponentNode ));
    spyOn(service, 'getRootComponent').and.returnValue(of());

    // when
    component.onUpdateNodeValueEvent(1, 1);

    // then
    expect(service.updateCompnentValue).toHaveBeenCalled();
    expect(service.getRootComponent).toHaveBeenCalled();
  });
});
